package me.ztiany.architecture.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.base.router.AppRouter
import com.app.base.router.RouterPath
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeDebugActivity : AppCompatActivity() {

    @Inject lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_debug)
    }

    fun openMain(view: View) {
        appRouter.build(RouterPath.Main.PATH).navigation()
    }

}
