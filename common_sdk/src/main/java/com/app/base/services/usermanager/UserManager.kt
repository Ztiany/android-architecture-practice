package com.app.base.services.usermanager

import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:38
 */
interface UserManager {

    /**登录之后，保存用户数据*/
    fun saveUser(user: User)

    /**同步获取获取用户是否已经登录，判断条件为[.user]返回值不为null*/
    fun userLogined(): Boolean

    /**同步获取用户信息。大部分情况下此方法从内存中读取用户信息，如果内存中没有用户信息才会从本地缓存加载。如果用户没有登录，则返回[User.NOT_LOGIN]*/
    fun user(): User

    /**观察用户信息，当用户信息被修改后，总是可以得到通知，这是一个全局多播的观察者，注意在不需要观察用户信息的时候取消订阅*/
    fun observableUser(): Flowable<User>

    /**退出登录*/
    fun logout(): Completable

}
