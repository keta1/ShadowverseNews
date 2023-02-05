@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.shadow)
    application
}

group = "icu.ketal"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("MainKt")
}

dependencies {
    implementation(libs.ktor.serialization.json)
    implementation(libs.kotlin.logging)
    implementation(libs.tgbotapi)
    implementation(libs.ktor.server)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    runtimeOnly(libs.exposed.jdbc)
    implementation(libs.exposed.kotlin.datetime)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.kotlinx.gettext)
    runtimeOnly(libs.sqlite.jdbc)
    runtimeOnly(libs.log4j.slf4j.impl)
    runtimeOnly(libs.log4j.core)
    runtimeOnly(libs.log4j.api)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
    target {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += "-Xcontext-receivers"
            }
        }
    }
}
