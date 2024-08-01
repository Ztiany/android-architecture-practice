package com.app.base.config

import android.content.Context
import com.android.base.image.glide.ProgressGlideModule
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import timber.log.Timber

@GlideModule
class CustomGlideModule : ProgressGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val dirCacheSize = 1024 * 1024 * 500
        builder.setDiskCache(
            ExternalPreferredCacheDiskCacheFactory(context, AppPrivateDirectories.IMAGE_CACHE_DIR, dirCacheSize.toLong())
        )
        val maxMemory = Runtime.getRuntime().maxMemory() / 8
        Timber.d("FtGlideModule image cache size:%d", maxMemory)
        builder.setMemoryCache(LruResourceCache(maxMemory.toInt().toLong()))
    }

}