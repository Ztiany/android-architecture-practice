package com.app.base.debug

import android.os.Bundle
import com.android.base.app.fragment.tools.inFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.R
import com.app.base.app.AppBaseActivity

/**
 * 仅用于调试版本
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date
 */
class DebugActivity : AppBaseActivity() {

    override fun provideLayout() = R.layout.app_base_activity

    override fun tintStatusBar() = false

    override fun setUpLayout(savedInstanceState: Bundle?) {
        super.setUpLayout(savedInstanceState)
        savedInstanceState.ifNull {
            inFragmentTransaction {
                addFragment(DebugFragment())
            }
        }
    }

}