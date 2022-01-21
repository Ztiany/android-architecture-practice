package com.vclusters.cloud.account.data

import com.app.base.services.usermanager.User
import kotlinx.coroutines.flow.Flow

interface AccountDataSource {

    fun login(phone: String, password: String): Flow<User>

    suspend fun historyUserList(): Flow<List<HistoryUser>>

    suspend fun deleteHistoryUser(historyUser: HistoryUser)

    suspend fun saveHistoryUser(historyUser: HistoryUser)

}