package com.tatq.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramResponse(
    @SerialName("ok")
    val ok: Boolean,
    @SerialName("result")
    val result: Result? = Result()
)
