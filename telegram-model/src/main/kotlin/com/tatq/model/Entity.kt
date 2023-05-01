package com.tatq.model

import kotlinx.serialization.Serializable

@Serializable
data class Entity(
    val type: String,
    val offset: Int,
    val length: Int
)
