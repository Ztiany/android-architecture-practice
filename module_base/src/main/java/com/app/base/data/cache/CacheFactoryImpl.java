package com.app.base.data.cache;

import android.content.Context;

import com.android.sdk.cache.CacheManager;
import com.android.sdk.cache.MMKVCacheImpl;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:35
 */
public class CacheFactoryImpl implements CacheFactory {

    @Override
    public CacheManager createCacheManager(Context context, String cacheId) {
        return new MMKVCacheImpl(context, cacheId);
    }

}
