package me.ztiany.architecture.main.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.base.router.AppRouter
import me.ztiany.architecture.main.api.MainModule
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.architecture.main.R
import javax.inject.Inject

@AndroidEntryPoint
class MainDebugActivity : AppCompatActivity() {

    @Inject lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_debug)
    }

    fun openMain(view: View) {
        appRouter.build(MainModule.PATH).navigation()
    }

}
