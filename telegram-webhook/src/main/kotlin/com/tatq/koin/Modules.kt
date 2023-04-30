package com.tatq.koin

import com.tatq.KProducerImpl
import org.apache.kafka.clients.producer.Producer
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

class Modules : KoinComponent {
    val module = module {
        single { (producer: Producer<String, String>) -> KProducerImpl(producer) }
    }
}
