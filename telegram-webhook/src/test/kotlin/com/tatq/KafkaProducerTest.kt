package com.tatq

import com.tatq.model.Chat
import com.tatq.model.TelegramMessage
import com.tatq.model.TelegramUpdate
import com.tatq.model.TelegramUser
import com.tatq.plugins.configureRouting
import com.tatq.plugins.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.serialization.StringSerializer
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.Test
import kotlin.test.assertEquals

private const val ENDPOINT = "/telegram"

class KafkaProducerTest : KoinTest {
    private lateinit var customClient: HttpClient
    private lateinit var mockProducer: MockProducer<String, String>

    @Before
    fun setUp() {
        mockProducer = MockProducer(true, StringSerializer(), StringSerializer())

        startKoin {
            modules(
                module {
                    single { KProducerImpl(mockProducer) }
                },
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
        mockProducer.close()
    }

    private fun testApplicationWrapper(testBlock: suspend ApplicationTestBuilder.() -> Unit) {
        testApplication {
            application {
                configureRouting()
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
    fun `Verify kafka message is published`() {
        val kafkaPublisher = get<KProducerImpl>()
        val telegramUpdate = TelegramUpdate(
            TelegramMessage(
                Chat("John", 0, "Doe", "", "jDoe"),
                0,
                TelegramUser("Jane", 0, "Doe", "jDoe"),
                0,
                "Hello World",
            ),
        )
        kafkaPublisher.send(telegramUpdate)

        // Verify the records
        val sentRecords = mockProducer.history()
        val jsonString = sentRecords[0].value()
        val payload = Json.decodeFromString<TelegramUpdate>(jsonString)
        assertEquals(1, sentRecords.size, "message size mismatch")
        assertEquals(expected = telegramUpdate, actual = payload)
    }

    @Test
    fun testPostRequest() = testApplicationWrapper {
        customClient.post(ENDPOINT) {
            contentType(ContentType.Application.Json)
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json,
            )

            val telegramUpdate = TelegramUpdate(
                TelegramMessage(
                    Chat("John", 0, "Doe", "", "jDoe"),
                    0,
                    TelegramUser("Jane", 0, "Doe", "jDoe"),
                    0,
                    "Hello World",
                ),
            )
            setBody(telegramUpdate)
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }

    @Test
    fun `POST request with invalid payload should return BadRequest`() = testApplicationWrapper {
        customClient.post(ENDPOINT) {
            contentType(ContentType.Application.Json)
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json,
            )
            val payload = """{"foo": "bar"}"""
            setBody(payload)
        }.apply {
            assertEquals(HttpStatusCode.BadRequest, status)
        }
    }

    private fun verifyPayloadCanBeParsed(payload: String) = testApplicationWrapper {
        customClient.post(ENDPOINT) {
            contentType(ContentType.Application.Json)
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json,
            )
            setBody(payload)
        }
            .apply {
                assertEquals(HttpStatusCode.OK, status)
            }
    }

    @Test
    fun `Verify text message payload`() {
        verifyPayloadCanBeParsed(normalMessage())
    }

    @Test
    fun `Verify forwarded message payload`() {
        verifyPayloadCanBeParsed(forwardedMessage())
    }

    @Test
    fun `Verify forwarded Channel Message payload`() {
        verifyPayloadCanBeParsed(forwardedChannelMessage())
    }

    @Test
    fun `Verify message With Reply payload`() {
        verifyPayloadCanBeParsed(messageWithReply())
    }

    @Test
    fun `Verify edited Message payload`() {
        verifyPayloadCanBeParsed(editedMessage())
    }

    @Test
    fun `Verify message With Entities payload`() {
        verifyPayloadCanBeParsed(messageWithEntities())
    }

    @Test
    fun `Verify message With Audio payload`() {
        verifyPayloadCanBeParsed(messageWithAudio())
    }

    @Test
    fun `Verify voice Message payload`() {
        verifyPayloadCanBeParsed(voiceMessage())
    }

    @Test
    fun `Verify message With Document payload`() {
        verifyPayloadCanBeParsed(messageWithDocument())
    }

    @Test
    fun `Verify inline Query payload`() {
        verifyPayloadCanBeParsed(inlineQuery())
    }

    @Test
    fun `Verify chosen Inline Query payload`() {
        verifyPayloadCanBeParsed(chosenInlineQuery())
    }

    @Test
    fun `Verify callback Query payload`() {
        verifyPayloadCanBeParsed(callbackQuery())
    }
}
