package me.ztiany.architecture.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.android.sdk.cache.CacheFactory
import com.android.sdk.cache.CacheManager
import com.app.base.router.AppRouter
import com.app.base.router.RouterPath
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeDebugActivity : AppCompatActivity() {

    @Inject
    lateinit var mCacheManager1: CacheManager
    @Inject
    lateinit var mCacheFactory: CacheFactory

    lateinit var mCacheManager2: CacheManager

    @Inject
    lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_debug)
        mCacheManager2 = mCacheFactory.createCacheManager(this, "hha")
    }

    fun openMain(view: View) {
        appRouter.build(RouterPath.Main.PATH).navigation()
    }

}
