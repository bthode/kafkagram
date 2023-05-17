package com.tatq.consumer

import org.apache.kafka.clients.consumer.Consumer

interface IProduceConsumer {
    fun createConsumer(bootstrapServersConfig: String): Consumer<String, String>
}
