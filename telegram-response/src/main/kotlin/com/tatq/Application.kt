package com.tatq

import com.tatq.consumer.CreateKafkaConsumer
import com.tatq.consumer.TelegramIngest
import com.tatq.plugins.configureHTTP
import com.tatq.plugins.configureSerialization
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStarted
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.runBlocking

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val telegramBotSecret = System.getenv("TELEGRAM_BOT_SECRET")
    requireNotNull(telegramBotSecret) { "Telegram API Secret Is Required" }
    val listenTopic = System.getenv("KAFKA_LISTEN_TOPIC")
    requireNotNull(listenTopic) { "Listen Topic Cannot Be Null" }
    val bootstrapServersConfig = System.getenv("BOOTSTRAP_SERVERS_CONFIG")
    requireNotNull(bootstrapServersConfig) { "Kafka Bootstrap Server Cannot Be Null" }

    configureHTTP()
    configureSerialization()
    val telegramIngest = TelegramIngest(
        httpEngine = HttpClient(engineFactory = CIO).engine,
        telegramBotSecret = telegramBotSecret,
        producerConsumer = CreateKafkaConsumer().createConsumer(bootstrapServersConfig),
        // TODO: We're not setting the listen topic in the real consumer
    )

    environment.monitor.subscribe(ApplicationStarted) {
        runBlocking {
            telegramIngest.consume()
        }
    }
}
