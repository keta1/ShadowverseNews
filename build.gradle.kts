import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

group = "icu.ketal"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val telegramBotApi = "3.2.6"
val ktorVersion = "2.1.1"
val exposedVersion = "0.39.2"

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")
    implementation("com.squareup.okhttp3:okhttp-tls:4.10.0")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("dev.inmo:tgbotapi:$telegramBotApi")
    implementation("dev.inmo:micro_utils.ktor.server:0.12.13")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:$exposedVersion")
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("name.kropp.kotlinx-gettext:kotlinx-gettext:0.5.0")
    runtimeOnly("org.xerial:sqlite-jdbc:3.39.3.0")
    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.19.0")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.19.0")
    runtimeOnly("org.apache.logging.log4j:log4j-api:2.19.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}

application {
    mainClass.set("MainKt")
}
