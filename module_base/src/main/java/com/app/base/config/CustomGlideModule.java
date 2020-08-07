package com.app.base.config;

import android.content.Context;

import com.android.base.imageloader.ProgressGlideModule;
import com.app.base.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.request.target.ViewTarget;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

@GlideModule
public class CustomGlideModule extends ProgressGlideModule {

    @Override
    public void applyOptions(@NotNull Context context, GlideBuilder builder) {
        //缓存位置，大小
        ViewTarget.setTagId(R.id.common_glide_item_tag_id);
        int dirCacheSize = 1024 * 1024 * 500;
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, DirectoryManager.IMAGE_CACHE_DIR, dirCacheSize));
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;
        Timber.d("FtGlideModule image cache size:%l", maxMemory);
        builder.setMemoryCache(new LruResourceCache((int) (maxMemory)));
    }

    @Override
    public void registerComponents(@NotNull Context context, @NotNull Glide glide, @NotNull Registry registry) {
        super.registerComponents(context, glide, registry);
        //内存策略
        glide.setMemoryCategory(MemoryCategory.NORMAL);
    }

}
