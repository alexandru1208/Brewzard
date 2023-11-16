package com.deskbird.datasource.local.internal

import com.deskbird.datasource.local.internal.dao.BreweryDao
import com.deskbird.datasource.local.internal.mapper.BreweryMapper
import com.deskbird.domain.data.LocalBreweriesDataSource
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.util.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class LocalBreweriesDataSourceImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val breweryDao: BreweryDao,
    private val breweryMapper: BreweryMapper
) : LocalBreweriesDataSource {

    override suspend fun isFavorite(breweryId: String): Boolean {
        return breweryDao.isFavorite(breweryId)
    }

    override suspend fun getBrewery(
        breweryId: String
    ): Brewery? = withContext(dispatchersProvider.io) {
        val breweryEntity = breweryDao.getBrewery(breweryId)
        return@withContext breweryEntity?.let(breweryMapper::mapToDomain)
    }

    override suspend fun updateFavorites(
        vararg brewery: Brewery
    ) = withContext(dispatchersProvider.io) {
        val breweries = breweryMapper.mapFromDomain(brewery.asList())
        breweryDao.update(*breweries.toTypedArray())
    }

    override suspend fun addToFavorites(
        brewery: Brewery
    ) = withContext(dispatchersProvider.io) {
        val breweryEntity = breweryMapper.mapFromDomain(brewery)
        breweryDao.insert(breweryEntity)
    }

    override suspend fun removeFromFavorites(
        brewery: Brewery
    ) = withContext(dispatchersProvider.io) {
        val breweriesEntity = breweryMapper.mapFromDomain(brewery)
        breweryDao.delete(breweriesEntity)
    }

    override suspend fun getFavorites(): List<Brewery> = withContext(dispatchersProvider.io) {
        val breweryEntities = breweryDao.getAll()
        return@withContext breweryMapper.mapToDomain(breweryEntities)
    }
}