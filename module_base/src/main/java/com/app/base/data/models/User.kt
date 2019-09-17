package com.app.base.data.models

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:31
 */
data class User(
        val userId: String,
        val userName: String
) {

    companion object {
        val NOT_LOGIN = User("", "")
    }

}
