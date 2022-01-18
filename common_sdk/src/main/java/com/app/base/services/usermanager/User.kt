package com.app.base.services.usermanager

import android.os.Parcelable
import com.android.common.apispec.SEX_MALE
import kotlinx.parcelize.Parcelize

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:31
 */
@Parcelize
data class User(
    /**用户id*/
    val id: String = "",
    /**昵称*/
    val nickname: String = "",
    /**头像url*/
    val avatar: String = "",
    /**性别*/
    val sex: Int = SEX_MALE,
    /**token*/
    val token: String = "",
) : Parcelable {

    companion object {
        val NOT_LOGIN = User()
    }

}

fun User?.isLogin() = this != null && this !== User.NOT_LOGIN
