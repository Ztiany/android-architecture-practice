package com.android.sdk.cache;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    static <T> T fromJson(String json, Class<T> clazz) {
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
}
