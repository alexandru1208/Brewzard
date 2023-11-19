package com.deskbird.brewzard.breweries.favorites

import com.deskbird.brewzard.domain.model.Brewery

data class FavoriteBreweriesScreenState(
    val breweries: List<Brewery> = emptyList(),
    val progressIndicatorVisible: Boolean = false,
    val errorVisible: Boolean = false,
)
