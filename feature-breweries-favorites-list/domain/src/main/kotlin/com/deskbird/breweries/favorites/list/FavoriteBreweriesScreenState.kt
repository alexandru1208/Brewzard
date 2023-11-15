package com.deskbird.breweries.favorites.list

import com.deskbird.domain.model.Brewery

data class FavoriteBreweriesScreenState(
    val breweries: List<Brewery> = emptyList(),
    val progressIndicatorVisible: Boolean = true,
    val errorVisible: Boolean = false
)