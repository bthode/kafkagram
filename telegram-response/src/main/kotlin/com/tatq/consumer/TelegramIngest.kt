package com.tatq.consumer

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.io.Closeable
import java.time.Duration

private const val TELEGRAM_API = "https://api.telegram.org/"
private const val SEND_MESSAGE = "/sendMessage"

class TelegramIngest(
    httpEngine: HttpClientEngine,
    telegramBotSecret: String,
    private val producerConsumer: Consumer<String, String>,
) : Closeable {
    private var sendMessageEndpoint: String
    private val httpClient = HttpClient(httpEngine)
    private lateinit var myJob: Job

    init {
        val botPrefix = "bot"
        sendMessageEndpoint = "$TELEGRAM_API$botPrefix$telegramBotSecret$SEND_MESSAGE".replace(
            "[\n\r]".toRegex(),
            "",
        ) // TODO: Why is our k8 secret including a newline?
    }

    suspend fun consume() =
        coroutineScope {
            while (isActive) {
                try {
                    val records = producerConsumer.poll(Duration.ofMillis(5000))
                    for (record: ConsumerRecord<String, String> in records) {
                        val json = record.value()
                        val data = Json.decodeFromString<OutgoingMessage>(json)
                        runBlocking {
                            sendMessageToTelegramBot(data)
                        }
                    }
                } catch (e: Exception) {
                    println("Error occurred: ${e.message}")
                } finally {
                    producerConsumer.close()
                }
                yield()
            }
        }

    private suspend fun sendMessageToTelegramBot(data: OutgoingMessage): Boolean {
        val dataToSend = Json.encodeToString(data)

        val response = httpClient.post(sendMessageEndpoint) {
            headers {
                append(HttpHeaders.ContentType, "application/json")
            }
            contentType(ContentType.Application.Json)
            setBody(dataToSend)
        }

        val telegramResponse = Json.decodeFromString<TelegramResponse>(response.body())
        return telegramResponse.ok
    }

    override fun close() {
        producerConsumer.close()
    }
}
