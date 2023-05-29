val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kotlinxSerializationVersion: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jmailen.kotlinter")
}

group = "com.tatq"
version = "0.0.1"
application {
    mainClass.set("com.tatq.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.ktor:ktor-client-cio")
    implementation("io.ktor:ktor-client-core")
    implementation("io.ktor:ktor-events")
    implementation("io.ktor:ktor-http")
    implementation("io.ktor:ktor-serialization-gson-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-serialization")
    implementation("io.ktor:ktor-server-compression-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-utils")
    implementation("org.apache.kafka:kafka-clients:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation(project(":telegram-model"))
    runtimeOnly("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("io.ktor:ktor-client-core")
    testImplementation("io.ktor:ktor-client-mock")
    testImplementation("io.ktor:ktor-io")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit") // buildHealth false positive, needs to be testImplementation
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.21")
    testImplementation("org.mockito:mockito-inline:5.2.0")
}
