package com.deskbird.brewzard.domain.model

data class Brewery(
    val id: String,
    val name: String,
    val websiteUrl: String?,
    val phone: String?,
    val type: BreweryType,
    val address: Address,
    val coordinates: Coordinates?,
    val isFavorite: Boolean,
)
