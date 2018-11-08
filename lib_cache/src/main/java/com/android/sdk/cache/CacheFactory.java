package com.android.sdk.cache;

import android.content.Context;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:32
 */
public interface CacheFactory {

    CacheManager createCacheManager(Context context, String cacheId);

}
