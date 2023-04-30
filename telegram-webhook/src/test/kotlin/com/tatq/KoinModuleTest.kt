package com.tatq

import org.apache.kafka.clients.producer.MockProducer
import org.apache.kafka.common.serialization.StringSerializer
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.check.checkModules
import kotlin.test.Test

class KoinModuleTest {
    @Test
    fun `check modules`() {
        val mockProducer = MockProducer(true, StringSerializer(), StringSerializer())
        checkModules { module { single { KProducerImpl(mockProducer) } } }
        stopKoin()
        mockProducer.close()
    }
}
