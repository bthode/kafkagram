package com.tatq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class OutgoingMessage(
    @SerialName("chat_id")
    val chatId: String,
    val text: String,
)
