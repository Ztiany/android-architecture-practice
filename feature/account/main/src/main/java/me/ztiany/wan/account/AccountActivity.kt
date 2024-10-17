package me.ztiany.wan.account

import android.os.Bundle
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.app.AppBaseActivity
import com.app.common.api.protocol.CannotShowExpiredDialogOnIt
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.account.presentation.login.SmsLoginFragment

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class AccountActivity : AppBaseActivity(), CannotShowExpiredDialogOnIt {

    override fun provideLayout() = com.app.base.R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment(SmsLoginFragment())
            }
        }
    }

}