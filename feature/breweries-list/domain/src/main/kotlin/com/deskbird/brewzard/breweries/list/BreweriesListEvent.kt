package com.deskbird.brewzard.breweries.list

sealed interface BreweriesListEvent {
    data class GoToDetails(val breweryId: String) : BreweriesListEvent
    data object ShowFetchMoreError : BreweriesListEvent
}
