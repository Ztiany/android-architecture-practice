package com.app.base.data.storage

import com.android.base.utils.security.AESUtils
import com.android.base.utils.security.GeneralPassword
import com.android.sdk.cache.encryption.Encipher
import timber.log.Timber
import java.nio.charset.Charset

/**
 *@author Ztiany
 */
object StorageEncipher : Encipher {

    private val PASSWORD = GeneralPassword("123456789987654321()")

    override fun encrypt(origin: String?): String? {
        Timber.d("encrypt %s", origin)
        if (origin.isNullOrEmpty()) {
            return origin
        }
        // 这次异常应该如何处理呢？在客户端，为了保证应用的正常使用（不崩溃），所以这里不继续抛出异常，而是返回原始数据。
        // 另外，如果要做的更好，则应该根据数据的安全级别来区别处理。
        return try {
            AESUtils.encryptDataToBase64(origin, AESUtils.Algorithm.AES, PASSWORD)
        } catch (e: Exception) {
            Timber.e(e, "encrypt error")
            origin
        }
    }

    override fun decrypt(encrypted: String?): String? {
        Timber.d("decrypt %s", encrypted)
        if (encrypted.isNullOrEmpty()) {
            return encrypted
        }
        return try {
            val encryptData = AESUtils.decryptDataFromBase64(encrypted, AESUtils.Algorithm.AES, PASSWORD)
            String(encryptData, Charset.defaultCharset())
        } catch (e: Exception) {
            Timber.e(e, "decrypt %s error", encrypted)
            encrypted
        }
    }

}