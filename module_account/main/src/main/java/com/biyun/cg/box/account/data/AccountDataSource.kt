package com.biyun.cg.box.account.data

import com.app.common.api.usermanager.User

interface AccountDataSource {

    suspend fun login(phone: String, password: String): User

}