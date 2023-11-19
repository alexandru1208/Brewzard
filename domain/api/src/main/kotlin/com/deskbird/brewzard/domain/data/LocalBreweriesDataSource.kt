package com.deskbird.brewzard.domain.data

import com.deskbird.brewzard.domain.model.Brewery
import kotlinx.coroutines.flow.Flow

interface LocalBreweriesDataSource {
    suspend fun isFavorite(breweryId: String): Boolean
    suspend fun getBrewery(breweryId: String): Brewery?
    suspend fun updateFavorites(breweries: List<Brewery>)
    suspend fun addToFavorites(brewery: Brewery)
    suspend fun removeFromFavorites(brewery: Brewery)
    fun observeFavorites(): Flow<List<Brewery>>
}
