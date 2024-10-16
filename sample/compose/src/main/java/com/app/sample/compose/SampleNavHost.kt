package com.app.sample.compose

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.app.sample.compose.ui.buttons.buttonComponentScreen
import com.app.sample.compose.ui.dialogs.dialogComponentScreen
import com.app.sample.compose.ui.list.flexibleListScreen
import com.app.sample.compose.ui.list.listScreen
import com.app.sample.compose.ui.state.refreshStateScreen
import com.app.sample.compose.ui.request.requestScreen
import com.app.sample.compose.ui.state.simpleStateScreen


@Composable
fun SampleNavGraph(
    modifier: Modifier = Modifier,
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        homeScreen(navController)

        // page samples
        simpleStateScreen(navController)
        refreshStateScreen(navController)
        listScreen(navController)
        flexibleListScreen(navController)
        requestScreen(navController)

        // component samples
        buttonComponentScreen(navController)
        dialogComponentScreen(navController)
    }
}