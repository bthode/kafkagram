package com.tatq.plugins

import com.tatq.koin.Modules
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(Modules().module)
    }
}
