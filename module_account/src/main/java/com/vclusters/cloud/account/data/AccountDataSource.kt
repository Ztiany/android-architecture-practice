package com.vclusters.cloud.account.data

import com.app.base.component.usermanager.User
import kotlinx.coroutines.flow.Flow

interface AccountDataSource {

    fun login(phone: String, password: String): Flow<User>

    fun historyUserList(): Flow<List<HistoryUser>>

    fun deleteHistoryUser(historyUser: HistoryUser)

    fun saveHistoryUser(historyUser: HistoryUser)

}