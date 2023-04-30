package com.tatq

import com.tatq.model.TelegramUpdate
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import org.koin.core.component.KoinComponent

private const val PUBLISHER_TOPIC = "telegram"

class KProducerImpl(private val producer: Producer<String, String>) : KoinComponent, KProducer {

    override fun send(update: TelegramUpdate) {
        producer.send(ProducerRecord(PUBLISHER_TOPIC, Json.encodeToString(update)))
    }
}
