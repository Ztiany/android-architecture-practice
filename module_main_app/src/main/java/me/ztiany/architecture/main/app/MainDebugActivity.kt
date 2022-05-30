package me.ztiany.architecture.main.app

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.base.router.AppRouter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainDebugActivity : AppCompatActivity() {

    @Inject lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_app)
    }

    fun openMain(view: View) {

    }

}