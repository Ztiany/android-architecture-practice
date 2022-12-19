package com.biyun.cg.box.account.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.common.api.router.AppRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountDebugActivity : AppCompatActivity() {

    @Inject lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity_debug)
    }

}
