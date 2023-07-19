package me.ztiany.wan.account

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.common.api.router.AppRouter
import com.app.common.api.router.withNavigator
import me.ztiany.wan.main.MainModuleNavigator
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class AccountInternalNavigator @Inject constructor(
    @ActivityContext context: Context,
    private val appRouter: AppRouter
) {

    private val host = context as AppCompatActivity

    fun exitAndToHomePage() {
        appRouter.getNavigator(MainModuleNavigator::class.java)?.openMain(host)
        host.supportFinishAfterTransition()
    }

    fun checkUserProtocol() {

    }

    fun checkPrivacyProtocol() {

    }

}