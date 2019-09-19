package me.ztiany.architecture.account

import android.graphics.Color
import androidx.appcompat.widget.AppCompatTextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.android.base.utils.android.views.newMMLayoutParams
import com.app.base.app.AppBaseActivity
import com.app.base.router.RouterPath

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2019-09-19 15:52
 */
@Route(path = RouterPath.Account.PATH)
class AccountActivity : AppBaseActivity() {

    override fun layout() = AppCompatTextView(this).apply {
        layoutParams = newMMLayoutParams()
        setTextColor(Color.RED)
        text = "登录注册模块"
    }

}