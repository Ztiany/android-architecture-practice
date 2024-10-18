package me.ztiany.wan.###template###

import android.os.Bundle
import me.ztiany.wan.###template###.presentation.$$$template$$$Fragment
import com.android.base.fragment.tool.doFragmentTransaction
import com.android.base.utils.common.ifNull
import com.app.base.app.SimpleAppBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class $$$template$$$Activity : SimpleAppBaseActivity() {

    override fun setUpLayout(savedInstanceState: Bundle?) {
        savedInstanceState.ifNull {
            doFragmentTransaction {
                addFragment($$$template$$$Fragment())
            }
        }
    }

}