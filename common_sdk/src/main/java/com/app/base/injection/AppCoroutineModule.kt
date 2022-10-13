package com.app.base.injection

import com.app.base.app.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
class AppCoroutineModule {

    /**
     * Following coroutine’s best practices, you might need to inject an application-scoped CoroutineScope in some classes to
     * [launch new coroutines that follow the app lifecycle or to make certain work outlive the caller’s scope](https://developer.android.com/kotlin/coroutines/coroutines-best-practices#create-coroutines-data-layer).
     *
     * for detail, refer to [Create an application CoroutineScope using Hilt.](https://medium.com/androiddevelopers/create-an-application-coroutinescope-using-hilt-dd444e721528)
     */
    @Singleton
    @ApplicationScope
    @Provides
    fun providesCoroutineScope(dispatcherProvider: DispatcherProvider): CoroutineScope = CoroutineScope(SupervisorJob() + dispatcherProvider.ui())

}
