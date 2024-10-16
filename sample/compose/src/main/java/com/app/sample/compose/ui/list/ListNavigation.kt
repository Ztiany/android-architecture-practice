package com.app.sample.compose.ui.list

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

private const val LIST_ROUTE = "list"
private const val FLEXIBLE_LIST_ROUTE = "flexible_list"

fun NavGraphBuilder.listScreen(navController: NavHostController) {
    composable(LIST_ROUTE) {
        Paging3Screen()
    }
}

fun NavGraphBuilder.flexibleListScreen(navController: NavHostController) {
    composable(FLEXIBLE_LIST_ROUTE) {
        Paging3Screen()
    }
}

fun NavHostController.navigateToListScreen() {
    navigate(LIST_ROUTE)
}

fun NavHostController.navigateToFlexibleListScreen() {
    navigate(FLEXIBLE_LIST_ROUTE)
}