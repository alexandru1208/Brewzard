package com.deskbird.breweries.list

sealed interface BreweriesScreenNavEvent {
    data class GoToDetails(val breweryId: String) : BreweriesScreenNavEvent
}