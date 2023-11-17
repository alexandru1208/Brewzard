package com.deskbird.breweries.list

import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType

data class BreweriesScreenState(
    val breweryTypes: List<BreweryType> = BreweryType.entries,
    val selectedType: BreweryType? = null,
    val breweries: List<Brewery> = emptyList(),
    val progressIndicatorVisible: Boolean = false,
    val errorVisible: Boolean = false,
    val fetchMoreErrorVisible: Boolean = false,
)