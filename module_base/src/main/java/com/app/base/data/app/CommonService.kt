package com.app.base.data.app

import com.app.base.data.api.SmsType
import com.app.base.data.models.SmsCodeResponse
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import java.io.File

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-10-12 18:09
 */
interface CommonService {

    fun sendSmsCode(account: String, @SmsType type: Int): Flowable<SmsCodeResponse>

}
