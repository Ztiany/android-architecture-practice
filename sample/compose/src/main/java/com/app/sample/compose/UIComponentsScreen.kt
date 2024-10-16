package com.app.sample.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.sample.compose.widget.PageButton

@Composable
fun UIComponentsScreen(
    onNavigation: OnHomeNavigation,
    onAction: OnHomeAction,
) {
    Column(Modifier.verticalScroll(rememberScrollState())) {
        PageButton("Button Components") {
            onNavigation(HomeNavigation.Buttons)
        }

        PageButton("Dialog Components") {
            onNavigation(HomeNavigation.Dialogs)
        }
    }
}