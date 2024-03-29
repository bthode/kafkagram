pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("io.ktor.plugin") version "2.3.4" apply false
    id("org.jmailen.kotlinter") version "3.16.0" apply false
    kotlin("jvm") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
}

rootProject.name = "kafkagram"
include("telegram-webhook", "telegram-response", "telegram-echo", "telegram-model")
