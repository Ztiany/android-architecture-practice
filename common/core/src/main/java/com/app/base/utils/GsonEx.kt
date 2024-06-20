package com.app.base.utils

import com.google.gson.JsonElement

fun JsonElement.toJsonString(): String {
    return if (isJsonObject) {
        asJsonObject.toString()
    } else if (isJsonArray) {
        asJsonArray.toString()
    } else {
        asString
    }
}