package com.app.base.services.usermanager

import android.os.Parcelable
import com.app.base.data.protocol.SEX_MALE
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
    val Sex: Int = SEX_MALE,
) : Parcelable {

    companion object {
        val NOT_LOGIN = User()
    }

}

fun User?.logined() = this != null && this !== User.NOT_LOGIN
