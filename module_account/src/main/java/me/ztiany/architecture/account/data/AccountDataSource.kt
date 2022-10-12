package me.ztiany.architecture.account.data

import com.android.common.api.usermanager.User

interface AccountDataSource {

    suspend fun login(phone: String, password: String): User

}