package com.android.sdk.cache;

import android.text.TextUtils;

import java.util.List;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:11
 */
final class CacheEntityImpl {

    static void putEntity(String key, Object entity, long cacheTime, CacheManager cacheManager) {
        CacheEntity cacheEntity = new CacheEntity(JsonUtils.toJson(entity), cacheTime);
        cacheManager.putString(key, JsonUtils.toJson(cacheEntity));
    }

    static <T> T getEntity(String key, Class<T> clazz, CacheManager cacheManager) {
        String cacheStr = cacheManager.getString(key);
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
            cacheManager.remove(key);
        }
        return null;
    }

    static <T> List<T> getEntities(String key, Class<T> clazz, CacheManager cacheManager) {
        String cacheStr = cacheManager.getString(key);
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
            cacheManager.remove(key);
        }
        return null;
    }

}
