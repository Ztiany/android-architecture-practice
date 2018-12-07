package com.android.sdk.cache;

import com.android.sdk.functional.Optional;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

/**
 * 缓存接口
 *
 * @author Ztiany
 * Date : 2016-10-24 21:59
 */
public interface Storage {

    void putEntity(String key, Object entity, long cacheTime);

    void putEntity(String key, Object entity);

    <T> T getEntity(String key, Class<T> clazz);

    /**
     * 如果使用 {@link #putEntity(String, Object)} 方法传入的是 list 类型，则需要使用此方法获取。
     */
    <T> List<T> getEntityList(String key, Class<T> clazz);

    /**
     * 如果使用 {@link #putEntity(String, Object)} 方法传入的是 map 类型，则需要使用此方法获取。
     */
    <K, V> Map<K, V> getEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz);

    ///////////////////////////////////////////////////////////////////////////
    // basic type
    ///////////////////////////////////////////////////////////////////////////

    void putString(String key, String value);

    String getString(String key, String defaultValue);

    String getString(String key);

    void putLong(String key, long value);

    long getLong(String key, long defaultValue);

    void putInt(String key, int value);

    int getInt(String key, int defaultValue);

    void putBoolean(String key, boolean value);

    boolean getBoolean(String key, boolean defaultValue);

    ///////////////////////////////////////////////////////////////////////////
    // utils
    ///////////////////////////////////////////////////////////////////////////

    void remove(String key);

    void clearAll();

    ///////////////////////////////////////////////////////////////////////////
    // react
    ///////////////////////////////////////////////////////////////////////////

    <T> Flowable<T> entity(String key, Class<T> clazz);

    <T> Flowable<Optional<T>> optionalEntity(String key, Class<T> clazz);

    <T> Flowable<List<T>> entityList(String key, Class<T> clazz);

    <T> Flowable<Optional<List<T>>> optionalEntityList(String key, Class<T> clazz);

    <K, V> Flowable<Map<K, V>> entityMap(String key, Class<K> keyClazz, Class<V> valueClazz);

    <K, V> Flowable<Optional<Map<K, V>>> optionalEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz);

}
