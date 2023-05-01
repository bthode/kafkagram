package com.tatq.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelegramDocument(
    @SerialName("file_id")
    val fileId: String? = null,
    @SerialName("file_name")
    val fileName: String? = null,
    @SerialName("file_size")
    val fileSize: Int? = null,
    @SerialName("mime_type")
    val mimeType: String? = null,
)
