val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val koinVersion: String by project
val kotlinxSerializationVersion: String by project

plugins {
//    kotlin("jvm") version "1.8.10"
//    id("io.ktor.plugin") version "2.2.4"
//    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
//    id("com.autonomousapps.dependency-analysis") version "1.19.0"
//    id("org.jmailen.kotlinter") version "3.14.0"
    kotlin("jvm")
    id("io.ktor.plugin")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jmailen.kotlinter") version "3.14.0"
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
    api("io.insert-koin:koin-core:$koinVersion")
    api("io.insert-koin:koin-test:$koinVersion")
    implementation("io.insert-koin:koin-core:$koinVersion")
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-test:$koinVersion")
    implementation("io.ktor:ktor-http:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-default-headers-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-resources:$ktorVersion")
    implementation("io.ktor:ktor-utils:$ktorVersion")
    implementation("org.apache.kafka:kafka-clients:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$kotlinxSerializationVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationVersion")
    implementation("org.slf4j:slf4j-api:2.0.5")
    runtimeOnly("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.insert-koin:koin-test:$koinVersion")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.0")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.10")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    implementation(project(":telegram-model"))
    testImplementation(project(":telegram-model"))
}


