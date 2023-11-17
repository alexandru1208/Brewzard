package com.deskbird.domain.data

import com.deskbird.domain.model.Brewery
import kotlinx.coroutines.flow.Flow

interface LocalBreweriesDataSource {
    suspend fun isFavorite(breweryId: String): Boolean
    suspend fun getBrewery(breweryId: String): Brewery?
    suspend fun updateFavorites(vararg brewery: Brewery)
    suspend fun addToFavorites(brewery: Brewery)
    suspend fun removeFromFavorites(brewery: Brewery)
    fun observeFavorites(): Flow<List<Brewery>>
}