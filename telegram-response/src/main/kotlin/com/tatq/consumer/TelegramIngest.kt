package com.tatq.consumer

import com.tatq.model.OutgoingMessage
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.io.Closeable
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.*

private const val TELEGRAM_API = "https://api.telegram.org/"
private const val SEND_MESSAGE = "/sendMessage"

class TelegramIngest : Closeable {
    private lateinit var sendMessageEndpoint: String
    private lateinit var consumer: KafkaConsumer<String, String>

    fun main() {
        val botPrefix = "bot"
        val telegramBotSecret = System.getenv("TELEGRAM_BOT_SECRET")
        sendMessageEndpoint = "$TELEGRAM_API$botPrefix$telegramBotSecret$SEND_MESSAGE".replace("[\n\r]".toRegex(), "") // TODO: Why is our k8 secret including a newline?

        val bootstrapServersConfig = System.getenv("BOOTSTRAP_SERVERS_CONFIG") ?: "127.0.0.1:9092"
        val listenTopic = System.getenv("KAFKA_LISTEN_TOPIC")
        val props = Properties()
        with(props) {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
            put(ConsumerConfig.GROUP_ID_CONFIG, "test-group")
            put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000)
        }

        consumer = KafkaConsumer<String, String>(props)
        consumer.subscribe(listOf(listenTopic))

        while (true) {
            try {
                val records = consumer.poll(Duration.ofMillis(5000))
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

    fun sendMessageToTelegramBot(data: OutgoingMessage) {
        val dataToSend = Json.encodeToString(data)

        val httpClient = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create(sendMessageEndpoint))
            .header("Content-Type", "application/json")
            .POST(
                HttpRequest.BodyPublishers.ofString(dataToSend),
            )
            .build()

        val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

        println("Response code: ${response.statusCode()}")
        println("Response body: ${response.body()}")
    }

    override fun close() {
        consumer.close()
    }
}
