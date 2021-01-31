package com.app.base.push

interface PushCallBack {

    fun onRegisterPushSuccess(token: String)

    fun onRegisterPushFail()

}