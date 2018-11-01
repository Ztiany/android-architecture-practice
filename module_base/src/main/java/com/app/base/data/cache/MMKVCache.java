package com.app.base.data.cache;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:25
 */
public class MMKVCache implements CacheManager {

    @Override
    public void putEntity(String key, Object entity) {

    }

    @Override
    public <T> T getEntity(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void putEntity(String key, Object entity, long cacheTime) {

    }

    @Override
    public void putString(String key, String value) {

    }

    @Override
    public String getString(String key, String defaultValue) {
        return null;
    }

    @Override
    public String getString(String key) {
        return null;
    }

    @Override
    public void putLong(String key, long value) {

    }

    @Override
    public long getLong(String key, long defaultValue) {
        return 0;
    }

    @Override
    public void putInt(String key, int value) {

    }

    @Override
    public int getInt(String key, int defaultValue) {
        return 0;
    }

    @Override
    public void putBoolean(String key, boolean value) {

    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return false;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void clearAll() {

    }
}
