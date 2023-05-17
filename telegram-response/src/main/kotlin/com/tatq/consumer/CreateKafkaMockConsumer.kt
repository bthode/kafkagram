package com.tatq.consumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.MockConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy

class CreateKafkaMockConsumer : ICreateKafkaConsumer {
    override fun createConsumer(bootstrapServersConfig: String): Consumer<String, String> {
        return MockConsumer(OffsetResetStrategy.EARLIEST)
    }
}
