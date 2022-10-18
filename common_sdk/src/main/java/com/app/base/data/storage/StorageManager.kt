package com.app.base.data.storage

import android.content.Context
import com.android.sdk.cache.Storage
import com.android.sdk.cache.StorageContext
import com.android.sdk.cache.encryption.Encipher
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton

private const val STABLE_CACHE_ID = "app-stable-cache-id"

/**
 * 全局存储管理器
 *
 *@author Ztiany
 */
@Singleton
class StorageManager @Inject internal constructor(
    @ApplicationContext private val context: Context
) {

    private val storageCache = HashMap<String, WeakReference<Storage>>()

    private val _stable: Storage by lazy {
        StorageContext.newStorageFactory(StorageContext.SP)
            .newBuilder(context)
            .storageId(STABLE_CACHE_ID)
            .build()
    }

    /** 全局默认永久缓存，不支持跨进程，用户退出后缓存不会被清理。 */
    fun stable() = _stable

    /**
     * 创建一个存储器。
     *
     * - [storageId] 缓存文件名。
     * - [encipher] 用于数据加密。
     */
    @Synchronized
    fun newStorage(
        storageId: String,
        encipher: Encipher? = null
    ): Storage {

        val weakReference = storageCache[storageId]
        if (weakReference != null) {
            val storage = weakReference.get()
            if (storage != null) {
                return storage
            }
        }

        val storage = StorageContext.newStorageFactory(StorageContext.SP)
            .newBuilder(context)
            .storageId(storageId)
            .encipher(encipher)
            .build()

        storageCache[storageId] = WeakReference(storage)

        return storage
    }

}