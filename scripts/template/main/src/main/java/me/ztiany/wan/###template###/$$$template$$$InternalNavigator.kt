package me.ztiany.wan.###template###

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.common.api.router.AppRouter
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class $$$template$$$InternalNavigator @Inject constructor(
    @ActivityContext context: Context,
    private val appRouter: AppRouter,
) {

    private val host = context as AppCompatActivity

}