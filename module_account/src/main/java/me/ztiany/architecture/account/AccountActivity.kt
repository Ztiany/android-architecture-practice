package me.ztiany.architecture.account

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.architecture.fragment.tools.doFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.app.AppBaseActivity
import com.app.base.app.CannotShowExpiredDialogOnIt
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.architecture.account.api.AccountModule
import me.ztiany.architecture.account.presentation.login.LoginFragment

/**
 *@author Ztiany
 */
@Route(path = AccountModule.PATH)
@AndroidEntryPoint
class AccountActivity : AppBaseActivity(), CannotShowExpiredDialogOnIt {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment(LoginFragment())
            }
        }
    }

}