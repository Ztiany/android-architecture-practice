package com.app.base.services.usermanager

import android.os.Parcelable
import com.android.common.apispec.UNAVAILABLE_FLAG
import kotlinx.parcelize.Parcelize

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:31
 */
@Parcelize
data class User(
    /**用户 id*/
    val id: Long = UNAVAILABLE_FLAG.toLong(),
    /**用户名*/
    val username: String = "",
    /**手机号*/
    val phone: String = "",
    /**鉴权标识*/
    val token: String = ""
) : Parcelable {

    companion object {
        val NOT_LOGIN = User()
    }

}

fun User?.isLogin() = this != null && this !== User.NOT_LOGIN
