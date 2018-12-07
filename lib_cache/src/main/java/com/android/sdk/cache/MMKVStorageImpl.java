package com.android.sdk.cache;

import android.content.Context;
import android.util.Log;

import com.android.sdk.functional.Optional;
import com.tencent.mmkv.MMKV;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 11:25
 */
public class MMKVStorageImpl implements Storage {

    private static final String TAG = MMKVStorageImpl.class.getSimpleName();

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private final MMKV mMmkv;

    public MMKVStorageImpl(Context context, String mmkvId) {
        this(context, mmkvId, false);
    }

    public MMKVStorageImpl(Context context, String mmkvId, boolean multiProcess) {

        if (INITIALIZED.compareAndSet(false, true)) {
            String rootDir = MMKV.initialize(context.getApplicationContext());
            Log.d(TAG, "MMKV initialized and rootDir is: " + rootDir);
        }

        int mode = multiProcess ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE;
        mMmkv = MMKV.mmkvWithID(mmkvId, mode);
    }

    @Override
    public void putString(String key, String value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public String getString(String key, String defaultValue) {
        try {
            return mMmkv.decodeString(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public String getString(String key) {
        try {
            return mMmkv.decodeString(key);
        } catch (Error error) {
            error.printStackTrace();
        }
        return null;
    }

    @Override
    public void putLong(String key, long value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public long getLong(String key, long defaultValue) {
        try {
            return mMmkv.decodeLong(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void putInt(String key, int value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getInt(String key, int defaultValue) {
        try {
            return mMmkv.decodeInt(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void putBoolean(String key, boolean value) {
        try {
            mMmkv.encode(key, value);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        try {
            return mMmkv.decodeBool(key, defaultValue);
        } catch (Error error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void remove(String key) {
        try {
            mMmkv.removeValueForKey(key);
        } catch (Error error) {
            error.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        mMmkv.clear();
    }

    @Override
    public void putEntity(String key, Object entity, long cacheTime) {
        CommonImpl.putEntity(key, entity, cacheTime, this);
    }

    @Override
    public void putEntity(String key, Object entity) {
        CommonImpl.putEntity(key, entity, 0, this);
    }

    @Override
    public <T> T getEntity(String key, Class<T> clazz) {
        return CommonImpl.getEntity(key, clazz, this);
    }

    @Override
    public <T> List<T> getEntityList(String key, Class<T> clazz) {
        return CommonImpl.getEntityList(key, clazz, this);
    }

    @Override
    public <K, V> Map<K, V> getEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz) {
        return CommonImpl.getEntityMap(key, keyClazz, valueClazz, this);
    }

    @Override
    public <T> Flowable<T> entity(String key, Class<T> clazz) {
        return CommonImpl.flowableEntity(key, clazz, this);
    }

    @Override
    public <T> Flowable<Optional<T>> optionalEntity(String key, Class<T> clazz) {
        return CommonImpl.flowableOptionalEntity(key, clazz, this);
    }

    @Override
    public <T> Flowable<List<T>> entityList(String key, Class<T> clazz) {
        return CommonImpl.flowableEntityList(key, clazz, this);
    }

    @Override
    public <T> Flowable<Optional<List<T>>> optionalEntityList(String key, Class<T> clazz) {
        return CommonImpl.flowableOptionalEntityList(key, clazz, this);
    }

    @Override
    public <K, V> Flowable<Map<K, V>> entityMap(String key, Class<K> keyClazz, Class<V> valueClazz) {
        return CommonImpl.flowableEntityMap(key, keyClazz, valueClazz, this);
    }

    @Override
    public <K, V> Flowable<Optional<Map<K, V>>> optionalEntityMap(String key, Class<K> keyClazz, Class<V> valueClazz) {
        return CommonImpl.flowableOptionalEntityMap(key, keyClazz, valueClazz, this);
    }

}
