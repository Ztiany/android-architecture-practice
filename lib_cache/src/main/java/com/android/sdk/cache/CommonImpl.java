package com.android.sdk.cache;

import android.text.TextUtils;

import com.android.sdk.functional.Optional;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:11
 */
final class CommonImpl {

    static void putEntity(String key, Object entity, long cacheTime, Storage storage) {
        CacheEntity cacheEntity = new CacheEntity(JsonUtils.toJson(entity), cacheTime);
        storage.putString(key, JsonUtils.toJson(cacheEntity));
    }

    private static String getCacheEntity(String key, Storage storage) {
        String cacheStr = storage.getString(key);
        if (TextUtils.isEmpty(cacheStr)) {
            return null;
        }

        CacheEntity cacheEntity = JsonUtils.fromJson(cacheStr, CacheEntity.class);

        if (cacheEntity == null) {
            return null;
        }

        if (cacheEntity.mCacheTime == 0) {
            return cacheEntity.mJsonData;
        }

        if (System.currentTimeMillis() - cacheEntity.mStoreTime < cacheEntity.mCacheTime) {
            return cacheEntity.mJsonData;
        } else {
            storage.remove(key);
        }
        return null;
    }

    static <T> T getEntity(String key, Class<T> clazz, Storage storage) {
        String cacheEntity = getCacheEntity(key, storage);
        if (cacheEntity != null) {
            return JsonUtils.fromJson(key, clazz);
        }
        return null;
    }

    static <T> List<T> getEntityList(String key, Class<T> clazz, Storage storage) {
        String cacheEntity = getCacheEntity(key, storage);
        if (cacheEntity != null) {
            return JsonUtils.parseArray(key, clazz);
        }
        return null;
    }

    static <V, K> Map<K, V> getEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz, Storage storage) {
        String cacheEntity = getCacheEntity(key, storage);
        if (cacheEntity == null) {
            return null;
        }
        return JsonUtils.parseMap(cacheEntity, keyClazz, valueClazz);
    }

    static <T> Flowable<T> flowableEntity(String key, Class<T> clazz, Storage storage) {
        return Flowable.defer(() -> {
            T entity = storage.getEntity(key, clazz);
            if (entity == null) {
                return Flowable.empty();
            } else {
                return Flowable.just(entity);
            }
        }).subscribeOn(Schedulers.io());
    }

    static <T> Flowable<Optional<T>> flowableOptionalEntity(String key, Class<T> clazz, Storage storage) {
        return Flowable.fromCallable(() -> Optional.ofNullable(storage.getEntity(key, clazz)))
                .subscribeOn(Schedulers.io());
    }

    static <T> Flowable<List<T>> flowableEntityList(String key, Class<T> clazz, Storage storage) {
        return Flowable.defer(() -> {
            List<T> entities = storage.getEntityList(key, clazz);
            if (entities == null) {
                return Flowable.empty();
            } else {
                return Flowable.just(entities);
            }
        }).subscribeOn(Schedulers.io());
    }

    static <T> Flowable<Optional<List<T>>> flowableOptionalEntityList(String key, Class<T> clazz, Storage storage) {
        return Flowable.fromCallable(() -> Optional.ofNullable(storage.getEntityList(key, clazz)))
                .subscribeOn(Schedulers.io());
    }

    static <K, V> Flowable<Map<K, V>> flowableEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz, Storage storage) {
        return Flowable.defer(() -> {
            Map<K, V> entityMap = storage.getEntityMap(key, keyClazz, valueClazz);
            if (entityMap == null) {
                return Flowable.empty();
            } else {
                return Flowable.just(entityMap);
            }
        }).subscribeOn(Schedulers.io());
    }

    static <K, V> Flowable<Optional<Map<K, V>>> flowableOptionalEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz, Storage storage) {
        return Flowable.fromCallable(() -> Optional.ofNullable(storage.getEntityMap(key, keyClazz, valueClazz)))
                .subscribeOn(Schedulers.io());
    }

}
