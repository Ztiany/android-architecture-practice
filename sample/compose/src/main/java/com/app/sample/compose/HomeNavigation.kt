package com.app.sample.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.app.sample.compose.ui.buttons.navigateToButtonComponentScreen
import com.app.sample.compose.ui.dialogs.navigateToDialogsScreen
import com.app.sample.compose.ui.list.navigateToFlexibleListScreen
import com.app.sample.compose.ui.list.navigateToListScreen
import com.app.sample.compose.ui.request.navigateToRequestHandlingScreen
import com.app.sample.compose.ui.state.navigateToRefreshStateScreen
import com.app.sample.compose.ui.state.navigateToSimpleStateScreen

internal const val MAIN_SCREEN_ROUTE = "main_screen"

fun NavGraphBuilder.homeScreen(navController: NavHostController) {
    composable(
        MAIN_SCREEN_ROUTE,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        HomeScreen(
            onNavigation = {
                doNavigation(navController, it)
            },
            onAction = {}
        )
    }
}

private fun doNavigation(navController: NavHostController, navigation: HomeNavigation) {
    when (navigation) {
        HomeNavigation.RequestState -> navController.navigateToRequestHandlingScreen()
        HomeNavigation.SimpleState -> navController.navigateToSimpleStateScreen()
        HomeNavigation.RefreshState -> navController.navigateToRefreshStateScreen()
        HomeNavigation.List -> navController.navigateToListScreen()
        HomeNavigation.FlexibleList -> navController.navigateToFlexibleListScreen()
        HomeNavigation.Dialogs -> navController.navigateToDialogsScreen()
        HomeNavigation.Buttons -> navController.navigateToButtonComponentScreen()
    }
}

sealed interface HomeAction
typealias OnHomeAction = (HomeAction) -> Unit

sealed interface HomeNavigation {
    data object RequestState : HomeNavigation
    data object SimpleState : HomeNavigation
    data object RefreshState : HomeNavigation
    data object List : HomeNavigation
    data object FlexibleList : HomeNavigation
    data object Dialogs : HomeNavigation
    data object Buttons : HomeNavigation
}
typealias OnHomeNavigation = (HomeNavigation) -> Unit