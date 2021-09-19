package com.android.architecture.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.app.base.services.usermanager.UserManager
import com.app.base.router.AppRouter
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-08-14 14:20
 */
@ActivityScoped
class MainNavigator @Inject constructor(
        @ActivityContext context: Context,
        private val appRouter: AppRouter,
        private val userManager: UserManager
) {

    private val host = context as AppCompatActivity

}