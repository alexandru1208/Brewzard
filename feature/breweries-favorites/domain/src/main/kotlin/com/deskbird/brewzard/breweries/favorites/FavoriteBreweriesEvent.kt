package com.deskbird.brewzard.breweries.favorites

sealed interface FavoriteBreweriesEvent {
    data class GoToDetails(val breweryId: String) : FavoriteBreweriesEvent
}
