package com.tatq.consumer

import com.tatq.model.OutgoingMessage
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.common.TopicPartition
import java.util.Collections.singleton
import kotlin.test.Test
import kotlin.test.assertEquals

class TelegramResponseTest {

    @Test
    fun `Verify Kafka message on the correct topic results in a request to the Telegram server`() {
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

        // TODO: Should centralize this data
        val telegramAPI = "https://api.telegram.org/"
        val botPrefix = "bot"
        val sendMessage = "/sendMessage"
        val telegramBotSecret = "value"
        val sendMessageEndpoint = "$telegramAPI$botPrefix$telegramBotSecret$sendMessage"

        val mockEngine = MockEngine { request ->
            if (request.url.toString() == sendMessageEndpoint) {
                respond(
                    content = ByteReadChannel(json),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                )
            } else {
                error("Unhandled ${request.url.encodedPath}")
            }
        }

        val listenTopic = "listen-topic"
        val consumer = CreateKafkaMockConsumer().createConsumer("bootstrap.server") as MockConsumer<String, String>

        val startOffsets: HashMap<TopicPartition, Long> = HashMap<TopicPartition, Long>()
        val topicPartition = TopicPartition(listenTopic, 0)
        startOffsets[topicPartition] = 0L
        consumer.updateBeginningOffsets(startOffsets)

        val telegramIngest = TelegramIngest(
            httpEngine = mockEngine,
            telegramBotSecret = telegramBotSecret,
            producerConsumer = consumer,
        )

        // With the MockConsumer, partition assignment is not handled by the Kafka cluster. So we have to manually assign them.
        consumer.assign(singleton(topicPartition))

        val incomingMessageJson = Json.encodeToString(OutgoingMessage("12345", "Hello World!"))
        val consumerRecord = ConsumerRecord<String, String>(listenTopic, 0, 0L, null, incomingMessageJson)
        consumer.addRecord(consumerRecord)

        runBlocking {
            val job = launch {
                telegramIngest.consume()
            }
            delay(100) // any non-zero value works locally
            job.cancel()
            job.join()
        }

        assertEquals(
            expected = 1,
            mockEngine.requestHistory.size,
            message = "No request made to the correct endpoint as specified in the MockEngine",
        )
        assertEquals(expected = 1, mockEngine.responseHistory.size, message = "No response from the MockEngine")
        assertEquals(
            expected = sendMessageEndpoint,
            actual = mockEngine.requestHistory.first().url.toString(),
            message = "Didn't send a request to the correct endpoint",
        )
        assertEquals(
            expected = HttpStatusCode.OK,
            actual = mockEngine.responseHistory.first().statusCode,
            message = "Didn't get back  ${HttpStatusCode.OK}",
        )
        mockEngine.close()
    }
}
