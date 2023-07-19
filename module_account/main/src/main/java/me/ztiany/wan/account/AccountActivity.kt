package me.ztiany.wan.account

import android.os.Bundle
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.utils.android.compat.SystemBarCompat
import com.android.base.utils.common.ifNull
import com.app.base.app.AppBaseActivity
import com.app.base.app.CannotShowExpiredDialogOnIt
import me.ztiany.wan.account.presentation.sms.SmsLoginFragment
import com.google.android.material.color.MaterialColors
import dagger.hilt.android.AndroidEntryPoint

/**
 *@author Ztiany
 */
@AndroidEntryPoint
class AccountActivity : AppBaseActivity(), CannotShowExpiredDialogOnIt {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        SystemBarCompat.setStatusBarColor(
            this,
            MaterialColors.getColor(this, android.R.attr.windowBackground, "windowBackground not provided.")
        )

        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment(SmsLoginFragment())
            }
        }
    }

}