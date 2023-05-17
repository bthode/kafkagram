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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
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
    private val telegramBotSecret: String,
    private val producerConsumer: Consumer<String, String>,
) : Closeable {
    private lateinit var sendMessageEndpoint: String
    private val httpClient = HttpClient(httpEngine)
    val myScope = CoroutineScope(Dispatchers.Default)
    private lateinit var myJob: Job

    suspend fun consume() {
        val botPrefix = "bot"
        sendMessageEndpoint = "$TELEGRAM_API$botPrefix$telegramBotSecret$SEND_MESSAGE".replace("[\n\r]".toRegex(), "") // TODO: Why is our k8 secret including a newline?

        myJob = myScope.launch {
            while (true) { // TODO: Test is just sitting in this while loop forever.
                try {
                    val records = producerConsumer.poll(Duration.ofMillis(5000))
                    for (record: ConsumerRecord<String, String> in records) {
                        val json = record.value()
                        val data = Json.decodeFromString<OutgoingMessage>(json)
                        sendMessageToTelegramBot(data)
                    }
                } catch (e: Exception) {
                    println("Error occurred: ${e.message}")
                }
            }
        }
    }

    fun abort() {
        myJob.cancel()
    }

    suspend fun sendMessageToTelegramBot(data: OutgoingMessage): Boolean {
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
