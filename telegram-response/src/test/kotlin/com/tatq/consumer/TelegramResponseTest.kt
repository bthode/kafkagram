package com.tatq.consumer

import com.tatq.model.OutgoingMessage
import com.tatq.plugins.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.common.TopicPartition
import java.util.*
import java.util.Collections.singleton
import kotlin.test.Test
import kotlin.test.assertEquals

private const val ENDPOINT = "/telegram"

class TelegramResponseTest {
    private lateinit var customClient: HttpClient

    private fun testApplicationWrapper(testBlock: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            application {
                configureSerialization()
            }
            createClient {
                install(ContentNegotiation) {
                    json()
                }
            }.also { customClient = it }
            testBlock()
        }
    }

    @Test
    fun testPostRequest() = testApplicationWrapper {
        customClient.post(ENDPOINT) {
            contentType(ContentType.Application.Json)
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json,
            )

            val telegramUpdate = OutgoingMessage("asdf", "asdf")
            setBody(telegramUpdate)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun sampleClientTest() {
        runBlocking {
            val json = """
    {
      "ok": true,
      "result": {
        "message_id": 1,
        "from": {
          "id": 12345678,
          "first_name": "YourBot",
          "username": "YourBot"
        },
        "chat": {
          "id": 1234567890,
          "first_name": "John",
          "last_name": "Doe",
          "username": "JohnDoe",
          "type": "private"
        },
        "date": 1459958199,
        "text": "Hello from Bot!"
      }
    }
            """.trimIndent()


            val mockEngine = MockEngine { _ ->
                respond(
                    content = ByteReadChannel(json),
//                    status = HttpStatusCode.OK,
                    status = HttpStatusCode.BadRequest,
                    headers = headersOf(HttpHeaders.ContentType, "application/json"),
                )
            }

            val listenTopic = "listen-topic"
            val consumer = CreateKafkaMockConsumer().createConsumer("bootstrap.server") as MockConsumer<String, String>

            val startOffsets: HashMap<TopicPartition, Long> = HashMap<TopicPartition, Long>()
            val topicPartition = TopicPartition(listenTopic, 0)
            startOffsets[topicPartition] = 0L
            consumer.updateBeginningOffsets(startOffsets)

            val telegramBotSecret = "value"
            val telegramIngest = TelegramIngest(
                httpEngine = mockEngine,
                telegramBotSecret = telegramBotSecret,
                producerConsumer = consumer,
            )

            consumer.assign(singleton(topicPartition)) // TODO This has to be set in product code as well.

            val incomingMessageJson = Json.encodeToString(OutgoingMessage("12345", "Hello World!"))
            val consumerRecord = ConsumerRecord<String, String>(listenTopic, 0, 0L, null, incomingMessageJson)
            consumer.addRecord(consumerRecord)

            telegramIngest.consume()
            Thread.sleep(6000)
        }
    }

//    class ApiClient(engine: HttpClientEngine) {
//        private val httpClient = HttpClient(engine) {
//            install(ContentNegotiation) {
//                json()
//            }
//        }
//
//        suspend fun getIp(): IpResponse = httpClient.get("https://api.ipify.org/?format=json").body()
//    }

//    @Serializable
//    data class IpResponse(val ip: String)
}
