package me.ztiany.wan.account.data

import com.app.common.api.usermanager.User

internal interface AccountDataSource {

    suspend fun smsLogin(phone: String, smsCode: String): User

    suspend fun sendLoginCode(phone: String)

}