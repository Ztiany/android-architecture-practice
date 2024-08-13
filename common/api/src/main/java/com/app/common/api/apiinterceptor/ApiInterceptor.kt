package com.app.common.api.apiinterceptor

import org.json.JSONObject

interface ApiInterceptor {

    fun interceptErrorResponse(
        errorCode: String,
        errorMsg: String,
        originResponse: JSONObject,
        newResponse: JSONObject,
    ): Boolean

}