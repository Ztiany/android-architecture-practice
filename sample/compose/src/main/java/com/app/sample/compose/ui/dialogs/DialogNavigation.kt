package com.app.sample.compose.ui.dialogs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private const val ROUTE_NAME = "dialog"

fun NavGraphBuilder.dialogComponentScreen(navController: NavHostController) {
    composable(ROUTE_NAME) {
        DialogsScreen()
    }
}

fun NavHostController.navigateToDialogsScreen() {
    navigate(ROUTE_NAME)
}

