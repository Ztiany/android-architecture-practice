package com.android.sdk.cache;

import com.android.sdk.functional.Optional;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * 缓存接口
 *
 * @author Ztiany
 * Date : 2016-10-24 21:59
 */
public interface Storage {

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

    <T> Observable<List<T>> observableEntities(String key, Class<T> clazz);

    <T> Flowable<List<T>> flowableEntities(String key, Class<T> clazz);

    <T> Observable<T> observableEntity(String key, Class<T> clazz);

    <T> Flowable<T> flowableEntity(String key, Class<T> clazz);

    <T> Observable<Optional<List<T>>> observableOptionalEntities(String key, Class<T> clazz);

    <T> Flowable<Optional<List<T>>> flowableOptionalEntities(String key, Class<T> clazz);

    <T> Observable<Optional<T>> observableOptionalEntity(String key, Class<T> clazz);

    <T> Flowable<Optional<T>> flowableOptionalEntity(String key, Class<T> clazz);

}
