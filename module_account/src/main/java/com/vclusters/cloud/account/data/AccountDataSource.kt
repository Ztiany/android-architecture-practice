package com.vclusters.cloud.account.data

import com.app.base.services.usermanager.User
import kotlinx.coroutines.flow.Flow

interface AccountDataSource {

    fun login(phone: String, password: String): Flow<User>

}