package com.tatq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReplyToMessage(
    @SerialName("chat")
    val chat: Chat? = Chat(),
    @SerialName("date")
    val date: Int? = 0,
    @SerialName("message_id")
    val messageId: Int? = 0,
    @SerialName("text")
    val text: String? = ""
)
