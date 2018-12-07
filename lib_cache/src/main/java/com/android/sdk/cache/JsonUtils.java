package com.android.sdk.cache;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 16:38
 */
final class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private final static Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    static String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return GSON.toJson(entity);
        } catch (Exception e) {
            Log.e(TAG, "JsonSerializer toJson error with: entity = " + entity, e);
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    static <T> T fromJson(String json, Type clazz) {
        try {
            if (clazz == String.class) {
                return (T) json;
            } else {
                return GSON.fromJson(json, clazz);
            }
        } catch (Exception e) {
            Log.e(TAG, "JsonSerializer fromJson error with: json = " + json + " class = " + clazz, e);
        }
        return null;
    }

    /**
     * 解析 array 类型的 json，该 json 的样式必须是 "[]" 类型的。
     */
    static <T> List<T> parseArray(String json, Class<T> cls) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            JsonElement element = new JsonParser().parse(json);
            if (!element.isJsonArray()) {
                throw new JsonParseException(json + " 不是 array，无法解析为数组");
            }
            JsonArray jsonArray = element.getAsJsonArray();
            if (jsonArray.size() == 0) {
                return Collections.emptyList();
            }
            List<T> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                String item = jsonArray.get(i).toString();
                list.add(fromJson(item, cls));
            }
            return list;
        } catch (Exception e) {
            Log.e(TAG, "JsonSerializer parseArray error with: json = " + json + " class = " + cls, e);
        }
        return null;
    }

    /**
     * 解析 map 类型的 json，该 json 的样式必须是 "{}" 类型的，且 Key 类型为基本类型或字符串，值必须类型相同。
     */
    static <K, V> Map<K, V> parseMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement element = jsonParser.parse(json);

            if (element == null) {
                return null;
            }

            Map<K, V> resultMap = new HashMap<>();
            JsonObject jsonObject = element.getAsJsonObject();

            JsonElement valueElement;

            for (String objKey : jsonObject.keySet()) {
                valueElement = jsonObject.get(objKey);
                K k = JsonUtils.fromJson(objKey, keyClazz);
                V v = JsonUtils.fromJson(valueElement.toString(), valueClazz);
                resultMap.put(k, v);
            }

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("JsonSerializer parseMap error with: json = " + json + " key class = " + keyClazz + " value class = " + valueClazz);
        }
        return null;
    }

}
