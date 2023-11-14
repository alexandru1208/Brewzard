package com.deskbird.datasource.local.internal

import com.deskbird.datasource.local.internal.dao.BreweriesDao
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
    private val breweriesDao: BreweriesDao,
    private val breweryMapper: BreweryMapper
) : LocalBreweriesDataSource {

    override suspend fun isFavorite(breweryId: String): Boolean {
        return breweriesDao.isFavorite(breweryId)
    }

    override suspend fun addOrUpdateFavorites(
        vararg brewery: Brewery
    ) = withContext(dispatchersProvider.io) {
        val breweries = breweryMapper.mapFromDomain(brewery.asList())
        breweriesDao.insert(*breweries.toTypedArray())
    }

    override suspend fun removeFromFavorites(
        brewery: Brewery
    ) = withContext(dispatchersProvider.io) {
        val breweriesEntity = breweryMapper.mapFromDomain(brewery)
        breweriesDao.delete(breweriesEntity)
    }

    override suspend fun getFavorites(): List<Brewery> {
        val breweryEntities = breweriesDao.getAll()
        return breweryMapper.mapToDomain(breweryEntities)
    }
}