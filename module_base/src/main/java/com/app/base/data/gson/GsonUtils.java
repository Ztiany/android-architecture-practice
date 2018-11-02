package com.app.base.data.gson;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.app.base.data.net.AutoGenTypeAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
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
 * Date: 2018-08-15
 */
public class GsonUtils {

    private final static Gson GSON = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            /*容错处理*/
            .registerTypeAdapter(int.class, new IntegerJsonDeserializer())
            .registerTypeAdapter(Integer.class, new IntegerJsonDeserializer())
            .registerTypeAdapter(double.class, new DoubleJsonDeserializer())
            .registerTypeAdapter(Double.class, new DoubleJsonDeserializer())
            .registerTypeAdapter(float.class, new FloatJsonDeserializer())
            .registerTypeAdapter(Float.class, new FloatJsonDeserializer())
            .registerTypeAdapter(String.class, new StringJsonDeserializer())
            /*根据注解反序列化抽象类或接口*/
            .registerTypeAdapterFactory(new AutoGenTypeAdapterFactory())
            .create();

    public static Gson gson() {
        return GSON;
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
            return GSON.toJson(entity);
        } catch (Exception e) {
            Timber.e(e, "JsonSerializer toJson error with: entity = %s error = %s", entity.toString());
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            if (clazz == String.class) {
                return (T) json;
            } else {
                return GSON.fromJson(json, clazz);
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
                return GSON.fromJson(json, type);
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
