package com.android.sdk.cache;

import android.text.TextUtils;

import com.android.sdk.functional.Optional;

import java.lang.reflect.Type;

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

    static <T> T getEntity(String key, Type clazz, Storage storage) {
        String cacheJson = getCacheEntity(key, storage);
        if (cacheJson != null) {
            return JsonUtils.fromJson(cacheJson, clazz);
        }
        return null;
    }

    static <T> Flowable<T> flowableEntity(String key, Type clazz, Storage storage) {
        return Flowable.defer(() -> {
            T entity = storage.getEntity(key, clazz);
            if (entity == null) {
                return Flowable.empty();
            } else {
                return Flowable.just(entity);
            }
        }).subscribeOn(Schedulers.io());
    }

    static <T> Flowable<Optional<T>> flowableOptionalEntity(String key, Type clazz, Storage storage) {
        return Flowable.fromCallable(() -> {
            T entity = storage.getEntity(key, clazz);
            return Optional.ofNullable(entity);
        }).subscribeOn(Schedulers.io());
    }


}
