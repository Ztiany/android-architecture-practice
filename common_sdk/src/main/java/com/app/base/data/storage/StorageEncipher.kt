package com.app.base.data.storage

import com.android.base.utils.security.AESUtils
import com.android.sdk.cache.encryption.Encipher
import timber.log.Timber
import java.nio.charset.Charset

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-03-17 17:51
 */
object StorageEncipher : Encipher {

    private val password = "123456789987654321()"

    override fun encrypt(origin: String?): String? {
        Timber.d("encrypt %s", origin)
        if (origin.isNullOrEmpty()) {
            return origin
        }
        return try {
            AESUtils.encryptDataToBase64(origin, AESUtils.AES, password)
        } catch (e: Exception) {
            origin
        }
    }

    override fun decrypt(encrypted: String?): String? {
        Timber.d("decrypt %s", encrypted)
        if (encrypted.isNullOrEmpty()) {
            return encrypted
        }
        return try {
            val encryptData = AESUtils.decryptDataFromBase64(encrypted, AESUtils.AES, password)
                    ?: return ""
            return String(encryptData, Charset.defaultCharset())
        } catch (e: Exception) {
            encrypted
        }
    }

}