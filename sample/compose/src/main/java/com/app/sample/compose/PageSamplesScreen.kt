package com.app.sample.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.sample.compose.widget.PageButton

@Composable
fun PageSamplesScreen(
    onNavigation: OnHomeNavigation,
    onAction: OnHomeAction,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        PageButton("Request Handing") {
            onNavigation(HomeNavigation.RequestState)
        }
        PageButton("Simple State") {
            onNavigation(HomeNavigation.SimpleState)
        }
        PageButton("Refresh State") {
            onNavigation(HomeNavigation.RefreshState)
        }
        PageButton("Paging List") {
            onNavigation(HomeNavigation.List)
        }
        PageButton("Flexible Paging List") {
            onNavigation(HomeNavigation.FlexibleList)
        }
    }
}