package com.deskbird.breweries.details

sealed interface BreweryDetailsEvent {
    data class GoToWebsite(val url: String) : BreweryDetailsEvent
    data class Call(val phoneNumber: String) : BreweryDetailsEvent
    data class ShowOnMap(val latitude: Float, val longitude: Float) : BreweryDetailsEvent
}