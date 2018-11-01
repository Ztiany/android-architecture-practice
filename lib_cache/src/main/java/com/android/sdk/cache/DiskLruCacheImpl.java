package com.android.sdk.cache;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class DiskLruCacheImpl implements CacheManager {

    private DiskLruCacheHelper mDiskLruCacheHelper;
    private static final int CACHE_SIZE = 50 * 1024 * 1024;//50M
    private final File mDir;
    private final int mSize;

    /**
     * @param context   上下文
     * @param cachePath 缓存文件
     */
    public DiskLruCacheImpl(@NonNull Context context, @NonNull File cachePath) {
        this(context, cachePath, CACHE_SIZE);
    }

    /**
     * @param context   上下文
     * @param cachePath 缓存文件
     * @param cacheSize 缓存大小，字节数
     */
    public DiskLruCacheImpl(@NonNull Context context, @NonNull File cachePath, int cacheSize) {
        mDir = cachePath;
        mSize = cacheSize;
        @SuppressWarnings("unused")
        boolean mkdirs = mDir.getParentFile().mkdirs();
    }

    private DiskLruCacheHelper getDiskLruCacheHelper() {
        if (mDiskLruCacheHelper == null || mDiskLruCacheHelper.isClosed()) {
            try {
                mDiskLruCacheHelper = new DiskLruCacheHelper(null, mDir, mSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mDiskLruCacheHelper;
    }

    @Override
    public void putString(String key, String value) {
        getDiskLruCacheHelper().put(buildKey(key), value);
    }

    @Override
    public String getString(String key, String defaultValue) {
        String result = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(result)) {
            result = defaultValue;
        }
        return result;
    }

    @Override
    public String getString(String key) {
        return getDiskLruCacheHelper().getAsString(buildKey(key));
    }

    @Override
    public void putLong(String key, long value) {
        getDiskLruCacheHelper().put(buildKey(key), String.valueOf(value));
    }

    @Override
    public long getLong(String key, long defaultValue) {
        String strLong = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(strLong)) {
            return defaultValue;
        }
        return Long.parseLong(strLong);
    }

    @Override
    public void putInt(String key, int value) {
        getDiskLruCacheHelper().put(buildKey(key), String.valueOf(value));
    }

    @Override
    public int getInt(String key, int defaultValue) {
        String strInt = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(strInt)) {
            return defaultValue;
        }
        return Integer.parseInt(strInt);
    }

    @Override
    public void putBoolean(String key, boolean value) {
        int bool = value ? 1 : 0;
        getDiskLruCacheHelper().put(buildKey(key), String.valueOf(bool));
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        String strInt = getDiskLruCacheHelper().getAsString(buildKey(key));
        if (TextUtils.isEmpty(strInt)) {
            return defaultValue;
        }
        return Integer.parseInt(strInt) == 1;
    }

    @Override
    public void putEntity(String key, Object entity, long cacheTime) {
        CacheEntityImpl.putEntity(key, entity, cacheTime, this);
    }

    @Override
    public void putEntity(String key, Object entity) {
        CacheEntityImpl.putEntity(key, entity, 0, this);
    }

    @Override
    public <T> T getEntity(String key, Class<T> clazz) {
        return CacheEntityImpl.getEntity(key, clazz, this);
    }

    @Override
    public <T> List<T> getEntities(String key, Class<T> clazz) {
        return CacheEntityImpl.getEntities(key, clazz, this);
    }

    @Override
    public void remove(String key) {
        getDiskLruCacheHelper().remove(buildKey(key));
    }

    @Override
    public void clearAll() {
        try {
            getDiskLruCacheHelper().delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildKey(String originKey) {
        return originKey;
    }

}
