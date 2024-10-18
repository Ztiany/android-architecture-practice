package me.ztiany.wan.###template###.data

import com.android.sdk.net.ServiceContext
import com.android.sdk.net.coroutines.CallResult
import com.app.common.api.dispatcher.DispatcherProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
internal class $$$template$$$Repository @Inject constructor(
    private val ###template###Api: ServiceContext<$$$template$$$Api>,
    private val dispatcherProvider: DispatcherProvider,
) {

    suspend fun load$$$template$$$(id: String): CallResult<$$$template$$$> {
        return withContext(dispatcherProvider.io()) {
            ###template###Api.apiCall {
                ###template###($$$template$$$Request(id))
            }
        }
    }

}