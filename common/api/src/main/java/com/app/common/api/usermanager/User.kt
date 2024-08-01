package com.app.common.api.usermanager

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Ztiany
 */
@Parcelize
data class User(
    val id: Long = 0,
    val uid: String = "",
    val isCertified: Int = 0,
    val headImgUrl: String = "",
    val phoneNumber: String = "",
    val userName: String = "",
    val token: String = "",
) : Parcelable {

    companion object {
        val NOT_LOGIN = User()
    }

}

fun User?.isLogin() = this != null && this !== User.NOT_LOGIN
