package com.tatq

import com.tatq.model.OutgoingMessage
import com.tatq.model.TelegramUpdate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Produced
import java.time.Duration
import java.util.*

fun main() {
    val json = Json { ignoreUnknownKeys = true }
    val bootstrapServersConfig = System.getenv("BOOTSTRAP_SERVERS_CONFIG") ?: "127.0.0.1:9092"
    val listenTopic = System.getenv("KAFKA_LISTEN_TOPIC") ?: "listen-topic"
    val publishTopic = System.getenv("KAFKA_PUBLISH_TOPIC") ?: "outgoing-topic"
    println("bootstrap: $bootstrapServersConfig, listen: $listenTopic, publish: $publishTopic")

    val props = Properties()
    with(props) {
        put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersConfig)
        put(StreamsConfig.APPLICATION_ID_CONFIG, "telegram-echo")
        put(StreamsConfig.STATESTORE_CACHE_MAX_BYTES_CONFIG, 0)
        put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE_V2)
    }

    val streamsBuilder = StreamsBuilder()
    println(streamsBuilder.toString())

//     TODO: Should ensure there is more content than just "/echo " so we don't send empty messages.
    streamsBuilder.stream<String, String>(listenTopic)
        .mapValues { v -> json.decodeFromString<TelegramUpdate>(v) }
        .filter { _, value -> value.message?.text?.startsWith("/echo ") ?: false }.mapValues { _, value ->
            val channel = value.message?.chat?.id.toString()
            val text = value.message?.text?.removePrefix("/echo ")
            val outgoingMessage = text?.let { OutgoingMessage(channel, it) }
            val messageJson = Json.encodeToString(outgoingMessage)
            messageJson
        }.to(publishTopic, Produced.with(Serdes.String(), Serdes.String()))

    println("Building Topology")
    val topology = streamsBuilder.build()
    println("Building Streams")
    val streams = KafkaStreams(topology, props)
    println("Starting Streams")
    streams.start()
    println("Streams Started")

    Runtime.getRuntime().addShutdownHook(Thread {
        println("Closing Streams")
        streams.close(Duration.ofSeconds(5))
    })
}
