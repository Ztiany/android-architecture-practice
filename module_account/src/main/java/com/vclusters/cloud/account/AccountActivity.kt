package com.vclusters.cloud.account

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.architecture.activity.BaseActivity
import com.android.base.architecture.fragment.tools.doFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.app.CannotShowExpiredDialogOnIt
import com.vclusters.cloud.account.api.AccountModule
import com.vclusters.cloud.account.presentation.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 15:52
 */
@Route(path = AccountModule.PATH)
@AndroidEntryPoint
class AccountActivity : BaseActivity(), CannotShowExpiredDialogOnIt {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment(LoginFragment())
            }
        }
    }

}