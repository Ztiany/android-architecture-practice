package me.ztiany.architecture.account

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.android.common.api.router.AppRouter
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import me.ztiany.architecture.main.api.MainModuleNavigator
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

}