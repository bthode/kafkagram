package com.tatq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramUser(
    @SerialName("first_name")
    val firstName: String? = "",
    @SerialName("id")
    val id: Int? = 0,
    @SerialName("last_name")
    val lastName: String? = "",
    @SerialName("username")
    val username: String? = "",
    @SerialName("is_bot")
    val isBot: Boolean = false,
)
