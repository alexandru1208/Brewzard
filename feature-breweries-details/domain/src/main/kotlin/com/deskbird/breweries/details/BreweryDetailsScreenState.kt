package com.deskbird.breweries.details

import com.deskbird.domain.model.Brewery

data class BreweryDetailsScreenState(
    val brewery: Brewery? = null,
    val progressIndicatorVisible: Boolean = false,
    val errorVisible: Boolean = false,
)