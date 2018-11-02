package com.app.base.config;

import android.content.Context;

import com.android.base.imageloader.ProgressGlideModule;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;

import timber.log.Timber;

@GlideModule
public class GWGlideModule extends ProgressGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //缓存位置，大小
        int dirCacheSize = 1024 * 1024 * 500;
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, DirectoryManager.IMAGE_CACHE_DIR, dirCacheSize));
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;
        Timber.d("FtGlideModule image cache size:" + maxMemory);
        builder.setMemoryCache(new LruResourceCache((int) (maxMemory)));
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        super.registerComponents(context, glide, registry);
        //内存策略
        glide.setMemoryCategory(MemoryCategory.NORMAL);
    }
}
