package com.deskbird.brewzard.breweries.details

import com.deskbird.brewzard.domain.model.Brewery

data class BreweryDetailsScreenState(
    val brewery: Brewery? = null,
    val progressIndicatorVisible: Boolean = false,
    val errorVisible: Boolean = false,
)
