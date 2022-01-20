package com.app.base.services.usermanager

import kotlinx.coroutines.flow.Flow

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:38
 */
interface UserManager {

    /**save user token after login succeeded.*/
    fun saveUserToken(token: String)

    /**get current user's token.*/
    fun getUserToken(): String

    /**sync user info.*/
    fun syncUserInfo(): Flow<User>

    /**check if there is an user login*/
    fun isUserLogin(): Boolean

    /**获取用户信息。如果用户没有登录，则返回[User.NOT_LOGIN]*/
    fun user(): User

    /**观察用户信息，当用户信息被修改后，总是可以得到通知，这是一个全局多播的观察者，注意在不需要观察用户信息的时候取消订阅*/
    fun subscribeUser(): Flow<User>

    /**退出登录*/
    fun logout()

}
