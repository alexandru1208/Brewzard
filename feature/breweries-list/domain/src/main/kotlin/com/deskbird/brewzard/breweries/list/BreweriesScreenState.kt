package com.deskbird.brewzard.breweries.list

import com.deskbird.brewzard.domain.model.Brewery
import com.deskbird.brewzard.domain.model.BreweryType

data class BreweriesScreenState(
    val breweryTypes: List<BreweryType> = BreweryType.entries,
    val selectedType: BreweryType? = null,
    val breweries: List<Brewery> = emptyList(),
    val progressIndicatorVisible: Boolean = false,
    val errorVisible: Boolean = false,
)
