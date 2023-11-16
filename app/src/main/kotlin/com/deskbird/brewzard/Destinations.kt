package com.deskbird.brewzard

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector
import com.deskbird.strings.R

sealed interface Destination {
    val route: String
}

sealed class BottomNavDestination(
    override val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector
) : Destination {
    data object Breweries : BottomNavDestination(
        "breweries_graph",
        R.string.breweries_type,
        Icons.Filled.List
    )

    data object Favorites : BottomNavDestination(
        "favorites_graph",
        R.string.favorites_type,
        Icons.Filled.Favorite
    )
}

sealed interface Screen : Destination {
    data object Breweries : Screen {
        override val route: String = "breweries"
    }

    data object Favorites : Screen {
        override val route: String = "favorites"
    }

    sealed class Details(private val domainName: String) : Screen {

        override val route: String = "$domainName/{$ARGUMENT_KEY}"
        fun createRoute(argument: String) = "$domainName/$argument"

        companion object {
            const val ARGUMENT_KEY = "breweryId"
        }

        data object Brewery : Details("brewery-details")
        data object Favorite : Details("favorite-details")
    }
}

