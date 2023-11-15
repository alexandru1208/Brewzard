package com.deskbird.brewzard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.deskbird.breweries.list.ui.BreweriesScreen
import com.deskbird.designsystem.theme.BrewzardThemeWithBackground
import com.deskbird.designsystem.util.DevicePreview

val bottomNavItems = listOf(
    BottomNavScreen.Breweries,
    BottomNavScreen.Favorites,
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(
                modifier = Modifier
                    .background(colorScheme.primaryContainer)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                ,
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
            startDestination = BottomNavScreen.Breweries.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavScreen.Breweries.route) {
                BreweriesScreen {

                }
            }
            composable(BottomNavScreen.Favorites.route) { }
        }
    }
}

@DevicePreview
@Composable
private fun Preview() = BrewzardThemeWithBackground {
    AppNavigation()
}