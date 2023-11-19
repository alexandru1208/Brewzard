package com.deskbird.domain.repository

import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import kotlinx.coroutines.flow.Flow

interface BreweryRepository {
    suspend fun getBreweries(
        page: Int,
        pageSize: Int,
        type: BreweryType? = null,
    ): List<Brewery>

    suspend fun getBrewery(breweryId: String): Brewery

    fun observeFavorites(): Flow<List<Brewery>>

    suspend fun addToFavorites(brewery: Brewery)

    suspend fun removeFromFavorites(brewery: Brewery)
}
