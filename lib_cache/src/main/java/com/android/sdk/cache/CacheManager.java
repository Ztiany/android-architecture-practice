package com.android.sdk.cache;

import java.util.List;

/**
 * App缓存实现
 *
 * @author Ztiany
 * Date : 2016-10-24 21:59
 */
public interface CacheManager {

    void putEntity(String key, Object entity);

    <T> T getEntity(String key, Class<T> clazz);

    /**
     * 如果使用 {@link #putEntity(String, Object)} 方法传入的是 list 类型，则需要使用此方法获取。
     */
    <T> List<T> getEntities(String key, Class<T> clazz);

    void putEntity(String key, Object entity, long cacheTime);

    void putString(String key, String value);

    String getString(String key, String defaultValue);

    String getString(String key);

    void putLong(String key, long value);

    long getLong(String key, long defaultValue);

    void putInt(String key, int value);

    int getInt(String key, int defaultValue);

    void putBoolean(String key, boolean value);

    boolean getBoolean(String key, boolean defaultValue);

    void remove(String key);

    void clearAll();

}
