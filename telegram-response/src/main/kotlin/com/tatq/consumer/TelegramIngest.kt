package com.tatq.consumer

import AppConfig.SEND_MESSAGE_ENDPOINT
import AppConfig.TELEGRAM_API
import com.tatq.model.OutgoingMessage
import com.tatq.model.TelegramResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.yield
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.io.Closeable
import java.time.Duration
import java.util.Collections.singleton

class TelegramIngest(
    httpEngine: HttpClientEngine,
    telegramBotSecret: String,
    private val producerConsumer: Consumer<String, String>,
) : Closeable {
    private var sendMessageEndpoint: String
    private val httpClient = HttpClient(httpEngine)

    init {
        val botPrefix = "bot"
        sendMessageEndpoint = "$TELEGRAM_API$botPrefix$telegramBotSecret$SEND_MESSAGE_ENDPOINT".replace(
            "[\n\r]".toRegex(),
            "",
        ) // TODO: Why is our k8 secret including a newline?
    }

    fun subscribeToTopic(topic: String) {
        println("Subscribing to topic $topic")
        producerConsumer.subscribe(singleton(topic))
    }

    suspend fun consume() =
        coroutineScope {
            println("Starting polling for Kafka messages.")
            while (isActive) {
                try {
                    val records = producerConsumer.poll(Duration.ofSeconds(2))
                    for (record: ConsumerRecord<String, String> in records) {
                        val json = record.value()
                        val data = Json.decodeFromString<OutgoingMessage>(json)
                        sendMessageToTelegram(data)
                    }
                } catch (e: Exception) {
                    println("Error occurred: ${e.message}")
                }
                yield()
            }
        }

    private suspend fun sendMessageToTelegram(outgoingMessage: OutgoingMessage): Boolean {
        val dataToSend = Json.encodeToString(outgoingMessage)

        val response = httpClient.post(sendMessageEndpoint) {
            headers {
                append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            }
            contentType(ContentType.Application.Json)
            setBody(dataToSend)
        }

        val telegramResponse = Json.decodeFromString<TelegramResponse>(response.body())
        return telegramResponse.ok
    }

    override fun close() {
        println("Closing Kafka consumer.")
        producerConsumer.close()
    }
}
