package com.app.base.data.cache;

import android.content.Context;

import com.android.sdk.cache.CacheManager;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:32
 */
public interface CacheFactory {

    CacheManager createCacheManager(Context context, String cacheId);

}
