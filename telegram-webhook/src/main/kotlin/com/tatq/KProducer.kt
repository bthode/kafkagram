package com.tatq

import com.tatq.model.TelegramUpdate

interface KProducer {
    fun send(update: TelegramUpdate)
}
