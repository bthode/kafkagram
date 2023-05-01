package com.tatq.model

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class ForwardDate(val name: String) {
    companion object {
        fun fromString(unixTimestamp: String): Instant? {
            return Instant.ofEpochSecond(unixTimestamp.toLong())
        }
    }
}
