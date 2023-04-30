val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotlinxSerializationVersion: String by project
val slf4j: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "com.tatq"
version = "0.0.1"
application {
    mainClass.set("com.tatq.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.apache.kafka:kafka-streams:3.4.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.slf4j:slf4j-api:$slf4j")
    implementation("org.slf4j:slf4j-log4j12:$slf4j")
    implementation(project(":telegram-model"))
    api(project(":telegram-model"))
    testImplementation(project(":telegram-model"))
}