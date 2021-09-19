package com.android.architecture.main.common

import com.android.base.app.jetpack.LiveBus
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2021-01-14 14:56
 */
@ActivityScoped
class MainEvents @Inject constructor() : LiveBus()