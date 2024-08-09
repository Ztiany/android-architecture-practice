package com.app.base.debug

import android.os.Bundle
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.R
import com.app.base.app.AppBaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Ztiany
 */
@AndroidEntryPoint
class DebugActivity : AppBaseActivity() {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment(DebugFragment())
            }
        }
    }

}