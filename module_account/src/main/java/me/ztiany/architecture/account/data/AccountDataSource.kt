package me.ztiany.architecture.account.data

import com.app.base.component.usermanager.User

interface AccountDataSource {

    suspend fun login(phone: String, password: String): User

}