package com.deskbird.domain.repository

import com.deskbird.domain.data.LocalBreweriesDataSource
import com.deskbird.domain.data.RemoteBreweriesDataSource
import com.deskbird.domain.di.ApplicationScope
import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject

class BreweryRepository @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val localDatasource: LocalBreweriesDataSource,
    private val remoteDataSource: RemoteBreweriesDataSource
) {

    suspend fun getBreweries(
        page: Int,
        pageSize: Int,
        type: BreweryType? = null
    ) = supervisorScope {
        val remoteBreweries = remoteDataSource.getBreweries(page, pageSize, type)
        val updatedBreweries = remoteBreweries.map {
            async { it.copy(isFavorite = localDatasource.isFavorite(it.id)) }
        }.awaitAll()
        updateIfNeeded(*updatedBreweries.toTypedArray())
        return@supervisorScope updatedBreweries
    }

    suspend fun getBrewery(breweryId: String): Brewery {
        return try {
            val remoteBrewery = remoteDataSource.getBrewery(breweryId)
            val isFavorite = localDatasource.isFavorite(breweryId)
            val updatedBrewery = remoteBrewery.copy(isFavorite = isFavorite)
            updateIfNeeded(updatedBrewery)
            updatedBrewery
        } catch (exception: DataSourceException) {
            localDatasource.getBrewery(breweryId) ?: throw exception
        }
    }

    fun getFavorites(): Flow<List<Brewery>> {
        return localDatasource.observeFavorites()
    }

    suspend fun addToFavorites(brewery: Brewery) {
        localDatasource.addToFavorites(brewery)
    }

    suspend fun removeFromFavorites(brewery: Brewery) {
        localDatasource.removeFromFavorites(brewery)
    }

    private fun updateIfNeeded(vararg brewery: Brewery) {
        applicationScope.launch {
            val favoriteBreweries = brewery.filter { it.isFavorite }
            localDatasource.updateFavorites(*favoriteBreweries.toTypedArray())
        }
    }
}