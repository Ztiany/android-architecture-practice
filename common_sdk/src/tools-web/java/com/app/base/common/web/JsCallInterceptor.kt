package com.app.base.common.web

import android.os.Bundle

interface JsCallInterceptor {

    fun intercept(method: String, args: Array<String>?, resultReceiver: ResultReceiver?): Boolean

}

interface BaseCustomJsCallInterceptor : JsCallInterceptor {

    fun onInit(host: BaseWebFragment, bundle: Bundle?)

}
