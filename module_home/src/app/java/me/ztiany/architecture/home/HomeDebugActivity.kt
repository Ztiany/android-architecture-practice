package me.ztiany.architecture.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.android.sdk.cache.Storage
import com.android.sdk.cache.StorageFactory
import com.app.base.router.AppRouter
import com.app.base.router.RouterPath
import dagger.android.AndroidInjection
import javax.inject.Inject

class HomeDebugActivity : AppCompatActivity() {

    @Inject
    lateinit var mStorage: Storage
    @Inject
    lateinit var mStorageFactory: StorageFactory

    lateinit var mStorage2: Storage

    @Inject
    lateinit var appRouter: AppRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_debug)
        mStorage2 = mStorageFactory.newBuilder(this).storageId("hha").build()
    }

    fun openMain(view: View) {
        appRouter.build(RouterPath.Main.PATH).navigation()
    }

}
