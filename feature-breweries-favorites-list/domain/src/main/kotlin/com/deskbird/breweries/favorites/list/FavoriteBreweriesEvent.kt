package com.deskbird.breweries.favorites.list

sealed interface FavoriteBreweriesEvent {
    data class GoToDetails(val breweryId: String) : FavoriteBreweriesEvent
}