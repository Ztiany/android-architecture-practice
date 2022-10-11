package com.android.common.api.usermanager

import kotlinx.coroutines.flow.Flow

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:38
 */
interface UserManager {

    /** save a user after login succeeded. */
    fun saveUser(user: User)

    /** obtain current user. return [User.NOT_LOGIN] if not login. */
    val user: User

    /** subscribe User‘s state. if any property of the current user changed, you will get noticed. */
    fun subscribeUser(): Flow<User>

    /** delete the current user's data. accessing [user] will return [User.NOT_LOGIN]*/
    fun logout()

}

/**check if there is an user login*/
fun UserManager.isUserLogin(): Boolean = user.isLogin()

