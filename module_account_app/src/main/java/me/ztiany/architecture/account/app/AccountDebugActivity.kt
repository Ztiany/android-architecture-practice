package me.ztiany.architecture.account.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.base.router.AppRouter
import com.vclusters.cloud.account.api.R
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
