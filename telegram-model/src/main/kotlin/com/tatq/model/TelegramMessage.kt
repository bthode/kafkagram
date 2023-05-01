package com.tatq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramMessage(
    @SerialName("chat") val chat: Chat? = Chat(),
    @SerialName("date") val date: Int? = 0,
    @SerialName("from") val telegramUser: TelegramUser? = TelegramUser(),
    @SerialName("message_id") val messageId: Int? = 0,
    @SerialName("text") val text: String? = "",
    @SerialName("forward_from") val forwardFrom: TelegramUser? = null,

    // TODO: Parse the long out into an Instant in the ForwardFrom class.
    @SerialName("forward_date") val forwardDate: Long? = null,
    val entities: List<Entity>? = null,
    @SerialName("document") val telegramDocument: TelegramDocument? = null,
    @SerialName("reply_to_message") val replyToMessage: ReplyToMessage? = null,
    @SerialName("audio") val audio: Audio? = null,
    val query: String? = null,
    val offset: String? = null,
    val voice: Voice? = null,
)
