package utils

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val defaultJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

internal inline fun <reified T> T.encodeToJson(json: Json = defaultJson): String {
    return json.encodeToString(this)
}

internal inline fun <reified T> String.decodeToDataClass(json: Json = defaultJson): T {
    return json.decodeFromString(this)
}
