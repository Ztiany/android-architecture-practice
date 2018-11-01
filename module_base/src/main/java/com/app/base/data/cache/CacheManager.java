package com.app.base.data.cache;

/**
 * App缓存：
 * <pre>
 *          1：缓存使用LRU淘汰算法。所以某些不应被自动清除的缓存不应该使用此工具存储
 *          2：英文环境和中文环境缓存时的会key自动加上环境标识，不同构建版本(debug,release)的缓存目录不一样
 * </pre>
 *
 * @author Ztiany
 *         Date : 2016-10-24 21:59
 */
public interface CacheManager {

    void putEntity(String key, Object entity);

    <T> T getEntity(String key, Class<T> clazz);

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
