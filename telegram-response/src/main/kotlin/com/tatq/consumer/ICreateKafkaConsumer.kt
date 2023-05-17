package com.tatq.consumer

import org.apache.kafka.clients.consumer.Consumer

interface ICreateKafkaConsumer {
    fun createConsumer(bootstrapServersConfig: String): Consumer<String, String>
}
