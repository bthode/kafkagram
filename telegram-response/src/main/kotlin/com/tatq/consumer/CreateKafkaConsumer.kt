package com.tatq.consumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import java.util.*

class CreateKafkaConsumer : ICreateKafkaConsumer {
    override fun createConsumer(bootstrapServersConfig: String): Consumer<String, String> {
        val props = Properties()
        with(props) {
            put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig)
            put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
            put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
            put(ConsumerConfig.GROUP_ID_CONFIG, "test-group")
            put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000)
        }
        return KafkaConsumer(props)
    }
}
