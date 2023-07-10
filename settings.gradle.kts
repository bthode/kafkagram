pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("io.ktor.plugin") version "2.3.2" apply false
    id("org.jmailen.kotlinter") version "3.15.0" apply false
    kotlin("jvm") version "1.8.22" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
}

rootProject.name = "kafkagram"
include("telegram-webhook", "telegram-response", "telegram-echo", "telegram-model")
