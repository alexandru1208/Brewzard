package com.deskbird.brewzard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.deskbird.breweries.details.ui.BreweryDetailsScreen
import com.deskbird.breweries.favorites.list.ui.FavoriteBreweriesScreen
import com.deskbird.breweries.list.ui.BreweriesScreen
import com.deskbird.designsystem.theme.BrewzardThemeWithBackground
import com.deskbird.designsystem.util.DevicePreview
import kotlinx.coroutines.launch

val bottomNavItems = listOf(
    BottomNavDestination.Breweries,
    BottomNavDestination.Favorites,
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbarMessage = { message: String ->
        scope.launch {
            snackbarHostState
                .showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Long
                )
        }
        Unit
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .background(colorScheme.primaryContainer)
                    .windowInsetsPadding(WindowInsets.navigationBars),
                backgroundColor = colorScheme.primaryContainer,
                elevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any {
                        it.route == screen.route
                    } == true
                    val contentColor = if (selected) {
                        colorScheme.onPrimaryContainer
                    } else {
                        Color.Gray
                    }
                    BottomNavigationItem(
                        selected = selected,
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = null,
                                tint = contentColor
                            )
                        },
                        label = {
                            Text(stringResource(screen.resourceId), color = contentColor)
                        },
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = BottomNavDestination.Breweries.route,
            Modifier.padding(
                PaddingValues(
                    top = 0.dp,
                    bottom = innerPadding.calculateBottomPadding(),
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )
            )
        ) {
            breweriesGraph(
                navController = navController,
                onShowMessage = showSnackbarMessage
            )
            favoritesGraph(
                navController = navController,
                onShowMessage = showSnackbarMessage
            )
        }
    }
}

private fun NavGraphBuilder.breweriesGraph(
    navController: NavController,
    onShowMessage: (String) -> Unit
) {
    navigation(
        startDestination = Screen.Breweries.route,
        route = BottomNavDestination.Breweries.route
    ) {
        composable(route = Screen.Breweries.route) {
            BreweriesScreen {
                navController.navigate(Screen.Details.Brewery.createRoute(it))
            }
        }
        composable(route = Screen.Details.Brewery.route) {
            BreweryDetailsScreen(
                onShowMessage = onShowMessage,
                onBackClick = navController::navigateUp
            )
        }
    }
}

private fun NavGraphBuilder.favoritesGraph(
    navController: NavController,
    onShowMessage: (String) -> Unit
) {
    navigation(
        startDestination = Screen.Favorites.route,
        route = BottomNavDestination.Favorites.route
    ) {
        composable(route = Screen.Favorites.route) {
            FavoriteBreweriesScreen {
                navController.navigate(Screen.Details.Favorite.createRoute(it))
            }
        }
        composable(route = Screen.Details.Favorite.route) {
            BreweryDetailsScreen(
                onShowMessage = onShowMessage,
                onBackClick = navController::navigateUp
            )
        }
    }
}

@DevicePreview
@Composable
private fun Preview() = BrewzardThemeWithBackground {
    AppNavigation()
}