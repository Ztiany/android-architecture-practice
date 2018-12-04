package com.app.base.data;

import android.text.TextUtils;

import com.android.sdk.net.gson.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-12-04 15:51
 */
public class JsonUtils {

    private JsonUtils() {
        throw new UnsupportedOperationException("instance it not need");
    }

    private static Gson getGson() {
        return GsonUtils.gson();
    }

    public static String createJson(Map<String, ?> map) {
        JSONObject jsonObject = new JSONObject();
        Set<? extends Map.Entry<String, ?>> entries = map.entrySet();
        try {
            for (Map.Entry<String, ?> entry : entries) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e(e, "JsonUtils create Params Error ");
        }
        return jsonObject.toString();
    }

    public static String createJson(String[] params) {

        Timber.i("JsonUtils process Json：" + Arrays.toString(params));

        JSONObject jsonObject = new JSONObject();

        try {
            int length = params.length / 2;
            for (int i = 0; i < length; i++) {
                jsonObject.put(params[2 * i], params[2 * i + 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.e("JsonUtils create Params Error " + e);
        }
        return jsonObject.toString();
    }

    public static String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return getGson().toJson(entity);
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer toJson error with: entity = %s", entity.toString());
        }
        return "";
    }

    /**
     * 属性添加到一个 json 对象中
     *
     * @param obj      字符串形式的 json 对象
     * @param key      key
     * @param property 属性，可以是 Boolean、Number、Character、String、Json对象、Json数组
     * @return 添加后的字符串，内容是一个 json 对象
     */
    public static String addProperty(String obj, String key, Object property) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement parse = jsonParser.parse(obj);
            if (parse.isJsonObject()) {
                JsonObject object = parse.getAsJsonObject();
                if (property instanceof Boolean) {
                    object.addProperty(key, (Boolean) property);
                } else if (property instanceof Character) {
                    object.addProperty(key, (Character) property);
                } else if (property instanceof Number) {
                    object.addProperty(key, (Number) property);
                } else if (property instanceof String) {
                    String str = (String) property;
                    if (isArray(str) || isObj(str)) {
                        object.add(key, jsonParser.parse(str));
                    } else {
                        object.addProperty(key, str);
                    }
                }
                return object.toString();
            }
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer addProperty error with: obj = %s  property = %s", obj, property.toString());
        }
        return obj;
    }


    /**
     * 元素添加到一个 json 数组中
     *
     * @param obj      字符串形式的 json 对象
     * @param property 属性，可以是 Boolean、Number、Character、String、Json对象、Json数组
     * @return 添加后的字符串，内容是一个 json 数组
     */
    public static String addElement(String obj, Object property) {
        try {
            JsonParser jsonParser = new JsonParser();
            JsonElement parse = jsonParser.parse(obj);
            if (parse.isJsonArray()) {
                JsonArray array = parse.getAsJsonArray();
                if (property instanceof Boolean) {
                    array.add((Boolean) property);
                } else if (property instanceof Character) {
                    array.add((Character) property);
                } else if (property instanceof Number) {
                    array.add((Number) property);
                } else if (property instanceof String) {
                    String str = (String) property;
                    if (isArray(str) || isObj(str)) {
                        array.add(jsonParser.parse(str));
                    } else {
                        array.add(str);
                    }
                }
                return array.toString();
            }
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer addProperty error with: obj = %s  property = %s", obj, property.toString());
        }
        return obj;
    }

    /**
     * 初略判断字符串是否为json array
     */
    public static boolean isArray(String str) {
        return str != null && !str.isEmpty() && str.startsWith("[") && str.endsWith("]");
    }

    /**
     * 初略判断字符串是否为json obj
     */
    public static boolean isObj(String str) {
        return str != null && !str.isEmpty() && str.startsWith("{") && str.endsWith("}");
    }


    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return (T) json;
            } else {
                return getGson().fromJson(json, clazz);
            }
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer fromJson error with: json = %s , class = %s", json, clazz.getName());
        }
        return null;
    }

    /**
     * 带有类型参数的泛型类型，应该使用下面方式，比如你想解析 List&lt;Foo&gt; 、 Map&lt;String,Foo&gt;、HttpResult&lt;Foo&gt; 等泛型类型。
     * <pre>{@code
     *         List<Foo> listFoo = fromType(json,new TypeToken<List<Foo>>(){}.getType());
     *         Map<String, Foo> mapFoo = fromType(json,new TypeToken<Map<String, Foo>>(){}.getType());
     *         HttpResult<Foo> httpResult = fromType(json,new TypeToken<HttpResult<Foo>>(){}.getType());
     * }
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromType(String json, Type type) {
        if (type == String.class) {
            return (T) json;
        } else {
            try {
                return getGson().fromJson(json, type);
            } catch (Exception e) {
                Timber.e(e, "JsonSerializer fromType error with: json = %s , type = %s", json, type);
                return null;
            }
        }
    }

    /**
     * 解析 array 类型的 json，该 json 的样式必须是 "[]" 类型的。
     */
    public static <T> List<T> parseArray(String json, Class<T> cls) {
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
            Timber.e(e, "JsonSerializer parseArray error with: json = %s , class = %s", json, cls);
        }
        return null;
    }


}
