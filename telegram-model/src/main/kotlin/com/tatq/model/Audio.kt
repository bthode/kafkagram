package com.tatq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Audio(
    @SerialName("duration")
    val duration: Int? = null,
    @SerialName("file_id")
    val fileId: String? = null,
    @SerialName("file_size")
    val fileSize: Int? = null,
    @SerialName("mime_type")
    val mimeType: String? = null,
    @SerialName("title")
    val title: String? = null
)
