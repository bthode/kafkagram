package com.tatq.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class From(
    @SerialName("first_name")
    val firstName: String? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("username")
    val username: String? = null
)