package me.ztiany.architecture.account.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.common.api.router.AppRouter
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
