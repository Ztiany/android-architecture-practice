package com.app.base.data.app

import com.android.sdk.net.core.service.ServiceFactory
import com.app.base.data.models.SmsCodeResponse
import io.reactivex.Flowable

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-12 18:10
 */
internal class CommonServiceImpl constructor(
        serviceFactory: ServiceFactory
) : CommonService {

    override fun sendSmsCode(account: String, type: Int): Flowable<SmsCodeResponse> {
        return Flowable.just(SmsCodeResponse("1000"))
    }

}