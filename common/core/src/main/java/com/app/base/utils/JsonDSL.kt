package com.app.base.utils

import com.google.gson.JsonElement
import org.json.JSONArray
import org.json.JSONObject

@DslMarker
annotation class JsonDSL

@JsonDSL
interface JsonObject {

    fun put(key: String, value: Any?)

    infix fun String.by(value: Any?) {
        when (value) {
            is Number -> {
                put(this, value)
            }

            is String -> {
                put(this, value)
            }

            is Boolean -> {
                put(this, value)
            }

            is Char -> {
                put(this, value)
            }

            is JsonElement -> {
                put(this, value)
            }

            else -> {
                throw UnsupportedOperationException()
            }
        }
    }

    infix fun String.by(build: JsonObject.() -> Unit) {
        put(this, newJsonObject().apply { build() })
    }

    infix fun String.byArray(build: JsonArray.() -> Unit) {
        put(this, newJsonArray().apply { build() })
    }

}

private class JsonObjectImpl : JsonObject {

    val real = JSONObject()

    override fun put(key: String, value: Any?) {
        when (value) {
            is JsonObjectImpl -> real.put(key, value.real)
            is JsonArrayImpl -> real.put(key, value.real)
            else -> real.put(key, value)
        }
    }

    override fun toString(): String {
        return real.toString()
    }

}

@JsonDSL
interface JsonArray {

    fun put(value: Any?)

    fun JsonArray.value(value: Any?) {
        put(value)
    }

    fun JsonArray.array(build: JsonArray.() -> Unit) {
        put(newJsonArray().apply { build() })
    }

    fun JsonArray.value(build: JsonObject.() -> Unit) {
        put(newJsonObject().apply { build() })
    }

}

private class JsonArrayImpl : JsonArray {

    val real = JSONArray()

    override fun put(value: Any?) {
        when (value) {
            is JsonObjectImpl -> real.put(value.real)
            is JsonArrayImpl -> real.put(value.real)
            else -> real.put(value)
        }
    }

    override fun toString(): String {
        return real.toString()
    }

}

private fun newJsonObject(): JsonObject = JsonObjectImpl()

private fun newJsonArray(): JsonArray = JsonArrayImpl()

/**
 * usage:
 *
 * ```
 * val json = buildJson {
 *         "name" by "Kotlin"
 *         "age" by 10
 *         "creator" by {
 *             "name" by "JetBrains"
 *             "age" by "21"
 *         }
 *         "attribute" byArray {
 *             value("1")
 *             value("2")
 *             value("3")
 *         }
 *     }
 * ```
 */
fun buildJson(build: JsonObject.() -> Unit) = run {
    newJsonObject().apply {
        build()
    }
}

/**
 * @see buildJson
 */
fun buildJsonArray(build: JsonArray.() -> Unit) = run {
    newJsonArray().apply {
        build()
    }
}