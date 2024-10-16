package com.app.sample.compose.ui.request

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.base.foundation.state.Idle
import com.app.base.compose.architect.request.RequestLoadingDialog
import com.app.base.compose.architect.request.RequestStateHandler

@Composable
fun RequestScreen(
    viewModel: RequestViewModel = hiltViewModel(),
) {
    val remarkState = viewModel.remarkState.collectAsStateWithLifecycle()
    val updateState = viewModel.updateRemarkState.collectAsStateWithLifecycle(Idle)

    LaunchedEffect(Unit) {
        viewModel.sendIntent(Query)
    }

    RequestStateHandler(remarkState) {
        onLoading {
            RequestLoadingDialog()
        }
    }

    Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {

    }
}