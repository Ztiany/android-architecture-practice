package com.app.base.debug

import android.os.Bundle
import com.android.base.app.activity.BaseActivity
import com.android.base.app.fragment.tools.inFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * 仅用于调试版本
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date
 */
@AndroidEntryPoint
class DebugActivity : BaseActivity() {

    override fun provideLayout() = R.layout.app_base_activity

    override fun setUpLayout(savedInstanceState: Bundle?) {
        savedInstanceState.ifNull {
            inFragmentTransaction {
                addFragment(DebugFragment())
            }
        }
    }

}