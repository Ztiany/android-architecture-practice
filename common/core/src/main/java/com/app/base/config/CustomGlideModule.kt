package com.app.base.config

import android.content.Context
import com.android.base.image.glide.ProgressGlideModule
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.MemoryCategory
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import timber.log.Timber

@GlideModule
internal class CustomGlideModule : ProgressGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val dirCacheSize = 1024 * 1024 * 500
        builder.setDiskCache(
            ExternalPreferredCacheDiskCacheFactory(context, AppPrivateDirectories.IMAGE_CACHE_DIR, dirCacheSize.toLong())
        )
        val maxMemory = Runtime.getRuntime().maxMemory() / 8
        Timber.d("FtGlideModule image cache size:%d", maxMemory)
        builder.setMemoryCache(LruResourceCache(maxMemory.toInt().toLong()))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        glide.setMemoryCategory(MemoryCategory.NORMAL)
    }

}
