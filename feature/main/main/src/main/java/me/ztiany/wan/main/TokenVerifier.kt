package me.ztiany.wan.main

import com.android.sdk.net.coroutines.executeApiCallNullable
import com.app.common.api.dispatcher.DispatcherProvider
import com.app.base.data.protocol.ApiHelper
import com.app.base.data.protocol.ApiResult
import com.app.common.api.network.ApiServiceFactoryProvider
import com.app.common.api.usermanager.User
import com.app.common.api.usermanager.UserManager
import com.app.common.api.usermanager.isLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.http.GET
import timber.log.Timber
import java.util.Date

private const val VERIFY_INTERVAL = 5 * 1000L

internal class TokenVerifier(
    private val userManager: UserManager,
    private val serviceFactoryProvider: ApiServiceFactoryProvider,
    private val scope: CoroutineScope,
    private val dispatcherProvider: DispatcherProvider,
) {

    private var job: Job? = null
    private var previousUser: User = User.NOT_LOGIN

    private val tokenVerifyApi by lazy {
        serviceFactoryProvider.getDefault().createDefault(TokenVerifyApi::class.java)
    }

    fun start() {
        userManager.userFlow()
            .onEach {
                if (!previousUser.isLogin() && it.isLogin()) {
                    startVerifyingTask()
                } else if (!it.isLogin()) {
                    stopVerifyingTask()
                }
                previousUser = it
            }
            .launchIn(scope)
    }

    private fun stopVerifyingTask() {
        Timber.d("stopVerifyingTask")
        job?.cancel()
        job = null
    }

    private fun startVerifyingTask() {
        stopVerifyingTask()
        Timber.d("startVerifyingTask")
        job = scope.launch(dispatcherProvider.computation()) {
            startLoopVerify()
        }
    }

    private suspend fun startLoopVerify() {
        while (true) {
            delay(VERIFY_INTERVAL)
            Timber.d("doCheck is called at ${Date()}")
            if (!doCheck()) {
                break
            }
        }
    }

    private suspend fun doCheck(): Boolean {
        return try {
            withContext(dispatcherProvider.io()) {
                executeApiCallNullable { tokenVerifyApi.verifyToken() }
                Timber.d("doCheck OK!")
                true
            }
        } catch (e: Exception) {
            Timber.e(e, "doCheck")
            !ApiHelper.isAuthenticationExpired(e)
        }
    }

}

private interface TokenVerifyApi {

    @GET("client/user/operation/verifyToken")
    suspend fun verifyToken(): ApiResult<Unit>

}