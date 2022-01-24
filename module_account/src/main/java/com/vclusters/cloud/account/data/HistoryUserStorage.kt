package com.vclusters.cloud.account.data

import com.android.base.utils.common.removeWhen
import com.android.base.utils.security.AESUtils
import com.android.sdk.cache.getEntity
import com.app.base.config.AppSettings
import com.app.base.data.storage.StorageManager
import javax.inject.Inject

data class HistoryUser(
    val phone: String,
    val password: String
)

private const val HISTORY_USER_KEY = "HISTORY_USER_KEY"

class HistoryUserStorage @Inject constructor(
    private val appSettings: AppSettings,
    storageManager: StorageManager
) {

    private val storage = storageManager.stableStorage()

    private fun historyUsersInternal(): List<HistoryUser> {
        return storage.getEntity(HISTORY_USER_KEY, emptyList())
    }

    fun historyUsers(): List<HistoryUser> {
        return historyUsersInternal().map {
            val decryptedPassword = AESUtils.decryptDataFromBase64ToString(it.password, AESUtils.AES, appSettings.aesKey)
            it.copy(password = decryptedPassword)
        }
    }

    fun deleteHistoryUser(user: HistoryUser): List<HistoryUser> {
        return historyUsers().toMutableList().also {
            it.removeWhen { ele ->
                ele.phone == user.phone
            }
            storage.putEntity(HISTORY_USER_KEY, it)
        }
    }

    fun addHistoryUser(user: HistoryUser): List<HistoryUser> {
        return deleteHistoryUser(user).toMutableList().also {
            it.add(user.copy(password = AESUtils.encryptDataToBase64(user.password, AESUtils.AES, appSettings.aesKey) ?: ""))
            storage.putEntity(HISTORY_USER_KEY, it)
        }
    }

}