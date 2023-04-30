package com.tatq

import com.tatq.model.TelegramUpdate
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.koin.core.parameter.parametersOf
import org.koin.ktor.ext.inject
import java.util.*

fun Route.telegramRoute() {
    val bootstrapServersConfig = System.getenv("BOOTSTRAP_SERVERS_CONFIG") ?: "127.0.0.1:9092"
    val props = Properties()
    with(props) {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig)
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    }

    val producer = KafkaProducer<String, String>(props)
    val kafkaPublisher by inject<KProducerImpl> {
        parametersOf(producer)
    }

    post("/telegram") {
        val update = call.receive<TelegramUpdate>()
        kafkaPublisher.send(update)
        call.respond(HttpStatusCode.OK)
    }
}
