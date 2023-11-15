package com.deskbird.brewzard

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.deskbird.strings.R

sealed class BottomNavScreen(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) {
    data object Breweries : BottomNavScreen(
        "breweries",
        R.string.breweries,
        Icons.Filled.List
    )

    data object Favorites : BottomNavScreen(
        "favorites",
        R.string.favorites,
        Icons.Filled.Favorite
    )
}