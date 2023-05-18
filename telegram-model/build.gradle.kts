plugins {
    id("kafkagram.kotlin-library-conventions")
    kotlin("plugin.serialization")
}
dependencies {
    api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.0")
}
