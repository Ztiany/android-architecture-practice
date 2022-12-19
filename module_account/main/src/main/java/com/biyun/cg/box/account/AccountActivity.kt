package com.biyun.cg.box.account

import android.os.Bundle
import com.android.base.architecture.fragment.tools.doFragmentTransaction
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.android.views.getColorCompat
import com.android.base.utils.common.ifNull
import com.app.base.app.AppBaseActivity
import com.app.base.app.CannotShowExpiredDialogOnIt
import dagger.hilt.android.AndroidEntryPoint
import com.biyun.cg.box.account.presentation.login.LoginFragment

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class AccountActivity : AppBaseActivity(), CannotShowExpiredDialogOnIt {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        SystemBarCompat.setStatusBarColor(this, getColorCompat(R.color.page_bg_color))
        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment(LoginFragment())
            }
        }
    }

}