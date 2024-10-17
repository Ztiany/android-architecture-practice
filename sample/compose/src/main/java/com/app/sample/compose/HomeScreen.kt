package com.app.sample.compose

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * for nested navigation, see:
 *
 *  - [Implementing Nested Navigation with Bottom Bar and Separate Navigation Graphs in Android Jetpack Compose](https://medium.com/@kezzieleo/implementing-nested-navigation-with-bottom-bar-and-separate-navigation-graphs-in-android-jetpack-13c77a99e994)
 *  - [Jetsnack/app/src/main/java/com/example/jetsnack/ui/navigation/JetsnackNavController.kt](https://github.com/android/compose-samples/blob/main/Jetsnack/app/src/main/java/com/example/jetsnack/ui/navigation/JetsnackNavController.kt)
 */
@Composable
fun HomeScreen(
    onNavigation: OnHomeNavigation,
    onAction: OnHomeAction,
) {
    val homeNavController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            HomeNavigationBar(homeNavController)
        }
    ) { paddingValues ->
        HomeNavHost(Modifier.padding(paddingValues), homeNavController, onNavigation, onAction)
    }
}

@Composable
private fun HomeNavigationBar(homeNavController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        BottomNavigationItem.entries.forEach { navigationItem ->
            val selected = currentDestination?.hierarchy?.any { it.route == navigationItem.route } == true
            NavigationBarItem(
                selected = selected,
                label = {
                    Text(text = stringResource(id = navigationItem.iconTextId))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = if (selected) navigationItem.selectedIconId else navigationItem.unselectedIconId),
                        contentDescription = null
                    )
                },
                onClick = {
                    homeNavController.navigateToBottomBarRoute(navigationItem.route)
                }
            )
        }
    }
}

@Composable
private fun HomeNavHost(
    modifier: Modifier = Modifier,
    homeNavController: NavHostController,
    onNavigation: OnHomeNavigation,
    onAction: OnHomeAction,
) {
    NavHost(
        navController = homeNavController,
        startDestination = BottomNavigationItem.PageSamples.route,
        modifier = modifier,
        // disable animation for tab switch, see: <https://issuetracker.google.com/issues/297258205#comment21>
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(BottomNavigationItem.PageSamples.route) {
            PageSamplesScreen(onNavigation, onAction)
        }

        composable(BottomNavigationItem.UIComponents.route) {
            UIComponentsScreen(onNavigation, onAction)
        }
    }
}

private fun NavHostController.navigateToBottomBarRoute(route: String) {
    if (route != currentDestination?.route) {
        navigate(route) {
            popUpTo(graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}

enum class BottomNavigationItem(
    val selectedIconId: Int,
    val unselectedIconId: Int,
    val iconTextId: Int,
    val route: String,
) {
    PageSamples(
        selectedIconId = R.drawable.sample_icon_pager_selected,
        unselectedIconId = R.drawable.sample_icon_pager_normal,
        iconTextId = R.string.sample_tab_pager,
        route = "page_samples",
    ),

    UIComponents(
        selectedIconId = R.drawable.sample_icon_component_selected,
        unselectedIconId = R.drawable.sample_icon_component_normal,
        iconTextId = R.string.sample_tab_component,
        route = "ui_components",
    ),
}