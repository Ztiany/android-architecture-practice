package com.app.base.injection

import com.app.common.api.apiinterceptor.ApiInterceptor
import com.app.common.api.router.Navigator
import com.app.common.api.appservice.AppService
import org.json.JSONObject

interface DummyAppService : AppService
class DummyAppServiceImpl : DummyAppService

interface DummyNavigator : Navigator
class DummyNavigatorImpl : DummyNavigator

internal class DummyApiInterceptorImpl : ApiInterceptor {

    override fun interceptErrorResponse(
        errorCode: String,
        errorMsg: String,
        originResponse: JSONObject,
        newResponse: JSONObject,
    ): Boolean {
        return false
    }

    companion object {
        const val CODE = "fake_code"
    }

}