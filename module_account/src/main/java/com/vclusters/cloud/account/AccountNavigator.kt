package com.vclusters.cloud.account

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.base.router.AppRouter
import com.vclusters.cloud.main.api.MainModule
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class AccountNavigator @Inject constructor(
    @ActivityContext context: Context,
    private val appRouter: AppRouter
) {

    private val host = context as AppCompatActivity

    fun exitAndToHomePage() {
        appRouter.build(MainModule.PATH).navigation()
        host.supportFinishAfterTransition()
    }

}