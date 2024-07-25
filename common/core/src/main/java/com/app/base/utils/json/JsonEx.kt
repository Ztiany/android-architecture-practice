package com.app.base.utils.json

import com.android.sdk.net.core.json.GsonUtils
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.lang.reflect.Type

private val gson: Gson = GsonUtils.gson()

fun Map<String, *>.toJson(): String {
    val jsonObject = JSONObject()
    val entries = entries
    try {
        for ((key, value) in entries) {
            jsonObject.put(key, value)
        }
    } catch (e: JSONException) {
        Timber.e(e, "JsonUtils create Params Error ")
    }
    return jsonObject.toString()
}

fun Array<String>.toJson(): String {
    Timber.i("JsonUtils process Json：%s", contentToString())
    val jsonObject = JSONObject()

    try {
        val length = size / 2
        for (i in 0 until length) {
            jsonObject.put(this[2 * i], this[2 * i + 1])
        }
    } catch (e: JSONException) {
        Timber.e(e, "JsonUtils create Params Error")
    }
    return jsonObject.toString()
}

fun Any.toJson(): String {
    if (this is String) {
        return this
    }
    try {
        return gson.toJson(this)
    } catch (e: Exception) {
        Timber.e(e, "JsonSerializer toJson error with: entity = %s", toString())
    }
    return ""
}

fun Map<String, *>.toJSONObject(): JSONObject {
    val jsonObject = JSONObject()
    for ((key, valueObj) in this) {
        val value = valueObj.toString()
        if (value.maybeJsonObject()) {
            try {
                jsonObject.put(key, JSONObject(value))
            } catch (e: JsonParseException) {
                jsonObject.put(key, value)
            }
        } else if (value.maybeJsonArray()) {
            try {
                jsonObject.put(key, JSONArray(value))
            } catch (e: JsonParseException) {
                jsonObject.put(key, value)
            }
        } else {
            jsonObject.put(key, value)
        }
    }
    return jsonObject
}

/**
 * 将属性添加到一个 JSON 对象中。注意，String 必须是一个 JSON 对象字符串。
 *
 * @param key      key
 * @param property 属性，可以是 Boolean、Number、Character、String、Json 对象、Json 数组
 * @return 添加后的字符串，内容是一个 json 对象
 */
fun String.addToJsonObject(key: String, property: Any): String {
    try {
        val jsonParser = JsonParser()
        val parse = jsonParser.parse(this)
        if (parse.isJsonObject) {
            val jsonObject = parse.asJsonObject
            if (property is Boolean) {
                jsonObject.addProperty(key, property)
            } else if (property is Char) {
                jsonObject.addProperty(key, property)
            } else if (property is Number) {
                jsonObject.addProperty(key, property)
            } else if (property is String) {
                if (maybeJsonArray() || maybeJsonObject()) {
                    try {
                        jsonObject.add(key, jsonParser.parse(property))
                    } catch (e: JsonParseException) {
                        jsonObject.addProperty(key, property)
                    }
                } else {
                    jsonObject.addProperty(key, property)
                }
            }
            return jsonObject.toString()
        }
    } catch (e: Exception) {
        Timber.e(e, "JsonSerializer addProperty error with: obj = %s  property = %s", this, property.toString())
    }
    return this
}

/**
 * 将属性添加到一个 JSON 对象中。注意，String 必须是一个 JSON 数组字符串。
 */
fun String.addToJsonArray(property: Any): String {
    try {
        val jsonParser = JsonParser()
        val parse = jsonParser.parse(this)
        if (parse.isJsonArray) {
            val array = parse.asJsonArray
            if (property is Boolean) {
                array.add(property)
            } else if (property is Char) {
                array.add(property)
            } else if (property is Number) {
                array.add(property)
            } else if (property is String) {
                if (property.maybeJsonArray() || property.maybeJsonObject()) {
                    try {
                        array.add(jsonParser.parse(property))
                    } catch (e: JsonParseException) {
                        array.add(property)
                    }
                } else {
                    array.add(property)
                }
            }
            return array.toString()
        }
    } catch (e: Exception) {
        Timber.e(e, "JsonSerializer addProperty error with: obj = %s  property = %s", this, property.toString())
    }
    return this
}

fun String?.maybeJson(): Boolean {
    return maybeJsonObject() || maybeJsonArray()
}

/**
 * 初略判断字符串是否为 json array
 */
fun String?.maybeJsonArray(): Boolean {
    return this != null && this.startsWith("[") && this.endsWith("]")
}

/**
 * 初略判断字符串是否为 json obj
 */
fun String?.maybeJsonObject(): Boolean {
    return this != null && this.startsWith("{") && this.endsWith("}")
}

fun <T> String?.deserializeJson(clazz: Class<T>): T? {
    if (this.isNullOrBlank()) {
        return null
    }
    try {
        return if (clazz == String::class.java) {
            @Suppress("UNCHECKED_CAST")
            this as T
        } else {
            gson.fromJson(this, clazz)
        }
    } catch (e: Exception) {
        Timber.e(e, "JsonSerializer fromJson error with: json = %s , class = %s", this, clazz.name)
    }
    return null
}

/**
 * 带有类型参数的泛型类型，应该使用下面方式，比如解析 List&lt;Foo&gt; 、 Map&lt;String,Foo&gt;、ApiResult&lt;Foo&gt; 等泛型类型的示例如下：
 *
 * ```
 * List<Foo> listFoo = fromType(json,new TypeToken<List<Foo>>(){}.getType());
 * Map<String, Foo> mapFoo = fromType(json,new TypeToken<Map<String, Foo>>(){}.getType());
 * ApiResult<Foo> httpResult = fromType(json,new TypeToken<ApiResult<Foo>>(){}.getType());
 *```
 */
fun <T> String?.deserializeJson(type: Type): T? {
    if (this.isNullOrBlank()) {
        return null
    }

    if (type === String::class.java) {
        @Suppress("UNCHECKED_CAST")
        return this as T
    } else {
        try {
            return gson.fromJson(this, type)
        } catch (e: Exception) {
            Timber.e(e, "JsonSerializer fromType error with: json = %s , type = %s", this, type)
            return null
        }
    }
}