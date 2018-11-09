package com.android.sdk.cache;

import android.text.TextUtils;

import com.android.sdk.functional.Optional;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
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

    static <T> T getEntity(String key, Class<T> clazz, Storage storage) {
        String cacheStr = storage.getString(key);
        if (TextUtils.isEmpty(cacheStr)) {
            return null;
        }
        CacheEntity cacheEntity = JsonUtils.fromJson(cacheStr, CacheEntity.class);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.mCacheTime == 0) {
            return JsonUtils.fromJson(cacheEntity.mJsonData, clazz);
        }
        if (System.currentTimeMillis() - cacheEntity.mStoreTime < cacheEntity.mCacheTime) {
            return JsonUtils.fromJson(cacheEntity.mJsonData, clazz);
        } else {
            storage.remove(key);
        }
        return null;
    }

    static <T> List<T> getEntities(String key, Class<T> clazz, Storage storage) {
        String cacheStr = storage.getString(key);
        if (TextUtils.isEmpty(cacheStr)) {
            return null;
        }
        CacheEntity cacheEntity = JsonUtils.fromJson(cacheStr, CacheEntity.class);
        if (cacheEntity == null) {
            return null;
        }
        if (cacheEntity.mCacheTime == 0) {
            return JsonUtils.parseArray(cacheEntity.mJsonData, clazz);
        }
        if (System.currentTimeMillis() - cacheEntity.mStoreTime < cacheEntity.mCacheTime) {
            return JsonUtils.parseArray(cacheEntity.mJsonData, clazz);
        } else {
            storage.remove(key);
        }
        return null;
    }

    static <T> Observable<List<T>> observableEntities(String key, Class<T> clazz, Storage storage) {
        return Observable.defer(() -> {
            List<T> entities = storage.getEntities(key, clazz);
            if (entities == null) {
                return Observable.empty();
            } else {
                return Observable.just(entities);
            }
        }).subscribeOn(Schedulers.io());
    }

    static <T> Flowable<List<T>> flowableEntities(String key, Class<T> clazz, Storage storage) {
        return Flowable.defer(() -> {
            List<T> entities = storage.getEntities(key, clazz);
            if (entities == null) {
                return Flowable.empty();
            } else {
                return Flowable.just(entities);
            }
        }).subscribeOn(Schedulers.io());
    }


    static <T> Observable<T> observableEntity(String key, Class<T> clazz, Storage storage) {
        return Observable.defer(() -> {
            T entity = storage.getEntity(key, clazz);
            if (entity == null) {
                return Observable.empty();
            } else {
                return Observable.just(entity);
            }
        }).subscribeOn(Schedulers.io());
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

    static <T> Observable<Optional<List<T>>> observableOptionalEntities(String key, Class<T> clazz, Storage storage) {
        return Observable.fromCallable(() -> Optional.ofNullable(storage.getEntities(key, clazz)))
                .subscribeOn(Schedulers.io());
    }

    static <T> Observable<Optional<T>> observableOptionalEntity(String key, Class<T> clazz, Storage storage) {
        return Observable.fromCallable(() -> Optional.ofNullable(storage.getEntity(key, clazz)))
                .subscribeOn(Schedulers.io());
    }

    static <T> Flowable<Optional<List<T>>> flowableOptionalEntities(String key, Class<T> clazz, Storage storage) {
        return Flowable.fromCallable(() -> Optional.ofNullable(storage.getEntities(key, clazz)))
                .subscribeOn(Schedulers.io());
    }

    static <T> Flowable<Optional<T>> flowableOptionalEntity(String key, Class<T> clazz, Storage storage) {
        return Flowable.fromCallable(() -> Optional.ofNullable(storage.getEntity(key, clazz)))
                .subscribeOn(Schedulers.io());
    }

}
