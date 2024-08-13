package me.ztiany.wan.main.home.data

import com.android.sdk.net.ServiceContext
import com.app.common.api.dispatcher.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    private val homeApiContext: ServiceContext<HomeApi>,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun loadRecommends() {
        withContext(dispatcherProvider.io()) {
            homeApiContext.apiCall {
                recommends()
            }
        }
    }

}