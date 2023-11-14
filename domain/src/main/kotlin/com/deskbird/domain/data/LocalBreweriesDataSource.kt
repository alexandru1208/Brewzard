package com.deskbird.domain.data

import com.deskbird.domain.model.Brewery

interface LocalBreweriesDataSource {
    suspend fun isFavorite(breweryId: String): Boolean
    suspend fun addToFavorites(brewery: Brewery)
    suspend fun removeFromFavorites(brewery: Brewery)
    suspend fun getFavorites(): List<Brewery>
}