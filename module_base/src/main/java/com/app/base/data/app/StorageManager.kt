package com.app.base.data.app

import android.content.Context
import com.android.sdk.cache.MMKVStorageFactoryImpl
import com.android.sdk.cache.Storage
import com.android.sdk.cache.TypeFlag
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-12-19 13:51
 */
class StorageManager internal constructor(
        private val context: Context,
        private val appDataSource: AppDataSource
) {

    companion object {
        private const val STABLE_CACHE_ID = "app-forever-cache-id"
        private const val USER_ASSOCIATED_CACHE_ID = "app-UserAssociated-default-cache-id"
        private const val ALL_USER_ASSOCIATED_CACHE_ID_KEY = "all_user_associated_cache_id_key"
        private const val EMPTY_DEVICE_ID = "empty_device_id"//avoid app crash when no device bound
    }

    private val storageFactory = MMKVStorageFactoryImpl()

    private val _userAssociated: Storage = storageFactory
            .newBuilder(context)
            .storageId(USER_ASSOCIATED_CACHE_ID)
            .build()

    private val _stable: Storage = storageFactory
            .newBuilder(context)
            .storageId(STABLE_CACHE_ID)
            .build()

    private val _userAssociatedIdList by lazy {
        _stable.getEntity<MutableList<String>>(
                ALL_USER_ASSOCIATED_CACHE_ID_KEY,
                object : TypeFlag<MutableList<String>>() {}.rawType)
                ?: mutableListOf()
    }

    private val storageCache = HashMap<String, WeakReference<Storage>>()

    /**  全局默认用户相关缓存，不支持跨进程，用户退出后缓存也会被清理。 */
    fun userStorage() = _userAssociated

    /** 全局默认永久缓存，不支持跨进程，用户退出后缓存不会被清理。 */
    fun stableStorage() = _stable

    @Synchronized
    fun newStorage(storageId: String, userAssociated: Boolean = false): Storage {
        if (userAssociated) {
            if (!_userAssociatedIdList.contains(storageId)) {
                _userAssociatedIdList.add(storageId)
                stableStorage().putEntity(ALL_USER_ASSOCIATED_CACHE_ID_KEY, _userAssociatedIdList)
            }
        }

        val weakReference = storageCache[storageId]

        if (weakReference != null) {
            val storage = weakReference.get()
            if (storage != null) {
                return storage
            }
        }

        val storage = storageFactory.newBuilder(context)
                .storageId(storageId)
                .build()

        storageCache[storageId] = WeakReference(storage)

        return storage
    }

    /**仅由[AppDataSource.logout]在退出登录时调用*/
    internal fun clearUserAssociated() {
        userStorage().clearAll()

        if (_userAssociatedIdList.isEmpty()) {
            return
        }
        for (cacheId in _userAssociatedIdList) {
            if (cacheId.isEmpty()) {
                continue
            }
            storageFactory.newBuilder(context).storageId(cacheId).build().clearAll()
            Timber.d("clear user associated cache：$cacheId")
        }
        _userAssociatedIdList.clear()

        stableStorage().remove(ALL_USER_ASSOCIATED_CACHE_ID_KEY)
    }

}