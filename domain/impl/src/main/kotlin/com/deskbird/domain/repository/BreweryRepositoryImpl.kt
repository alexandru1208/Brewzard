package com.deskbird.domain.repository

import com.deskbird.domain.data.LocalBreweriesDataSource
import com.deskbird.domain.data.RemoteBreweryDataSource
import com.deskbird.domain.di.ApplicationScope
import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class BreweryRepositoryImpl @Inject constructor(
    @ApplicationScope private val applicationScope: CoroutineScope,
    private val localDatasource: LocalBreweriesDataSource,
    private val remoteDataSource: RemoteBreweryDataSource,
) : BreweryRepository {

    override suspend fun getBreweries(
        page: Int,
        pageSize: Int,
        type: BreweryType?,
    ) = coroutineScope {
        val remoteBreweries = remoteDataSource.getBreweries(page, pageSize, type)
        val updatedBreweries = remoteBreweries.map { remoteBrewery ->
            async { updateWithFavorite(remoteBrewery) }
        }.awaitAll()
        updateLocalIfNeeded(updatedBreweries)
        return@coroutineScope updatedBreweries
    }

    override suspend fun getBrewery(breweryId: String): Brewery = try {
        val remoteBrewery = remoteDataSource.getBrewery(breweryId)
        val updatedBrewery = updateWithFavorite(remoteBrewery)
        updateLocalIfNeeded(listOf(updatedBrewery))
        updatedBrewery
    } catch (exception: DataSourceException) {
        localDatasource.getBrewery(breweryId) ?: throw exception
    }

    override fun observeFavorites(): Flow<List<Brewery>> {
        return localDatasource.observeFavorites()
    }

    override suspend fun addToFavorites(brewery: Brewery) {
        localDatasource.addToFavorites(brewery)
    }

    override suspend fun removeFromFavorites(brewery: Brewery) {
        localDatasource.removeFromFavorites(brewery)
    }

    private suspend fun updateWithFavorite(brewery: Brewery): Brewery {
        val isFavorite = localDatasource.isFavorite(brewery.id)
        return brewery.copy(isFavorite = isFavorite)
    }

    private fun updateLocalIfNeeded(breweries: List<Brewery>) {
        applicationScope.launch {
            val favoriteBreweries = breweries.filter { it.isFavorite }
            localDatasource.updateFavorites(favoriteBreweries)
        }
    }
}
