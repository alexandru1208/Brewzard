package com.deskbird.breweries.favorites.list

sealed interface FavoriteBreweriesScreenNavEvent {
    data class GoToDetails(val breweryId: String) : FavoriteBreweriesScreenNavEvent
}