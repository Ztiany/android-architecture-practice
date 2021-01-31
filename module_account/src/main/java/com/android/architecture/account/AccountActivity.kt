package com.android.architecture.account

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.app.base.app.AppBaseActivity
import com.app.base.app.CannotShowExpiredDialogOnIt
import com.app.base.router.RouterPath
import dagger.hilt.android.AndroidEntryPoint

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 15:52
 */
@Route(path = RouterPath.Account.PATH)
@AndroidEntryPoint
class AccountActivity : AppBaseActivity(), CannotShowExpiredDialogOnIt {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        super.setUpLayout(savedInstanceState)
    }

}