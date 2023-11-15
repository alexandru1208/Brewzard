package com.deskbird.domain.repository

import com.deskbird.domain.data.LocalBreweriesDataSource
import com.deskbird.domain.data.RemoteBreweriesDataSource
import com.deskbird.domain.di.ApplicationScope
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class BreweriesRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val localDatasource: LocalBreweriesDataSource,
    private val remoteDataSource: RemoteBreweriesDataSource
) {

    suspend fun getBreweries(page: Int, type: BreweryType? = null) = supervisorScope {
        val remoteBreweries = remoteDataSource.getBreweries(page, type)
        val updatedBreweries = remoteBreweries.map {
            async { it.copy(isFavorite = localDatasource.isFavorite(it.id)) }
        }.awaitAll()
        updateFavorites(updatedBreweries)
        return@supervisorScope updatedBreweries
    }

    suspend fun getFavorites(): List<Brewery> {
        return localDatasource.getFavorites()
    }

    suspend fun addToFavorites(brewery: Brewery) {
        localDatasource.addOrUpdateFavorites(brewery)
    }

    suspend fun removeFromFavorites(brewery: Brewery) {
        localDatasource.removeFromFavorites(brewery)
    }

    private fun updateFavorites(breweries: List<Brewery>) {
        applicationScope.launch {
            val favoriteBreweries = breweries.filter { it.isFavorite }
            localDatasource.addOrUpdateFavorites(*favoriteBreweries.toTypedArray())
        }
    }
}