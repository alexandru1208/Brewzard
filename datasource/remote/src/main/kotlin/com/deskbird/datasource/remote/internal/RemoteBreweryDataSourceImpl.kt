package com.deskbird.datasource.remote.internal

import com.deskbird.datasource.remote.internal.mapper.BreweryMapper
import com.deskbird.datasource.remote.internal.mapper.BreweryTypeMapper
import com.deskbird.domain.data.RemoteBreweryDataSource
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.domain.util.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class RemoteBreweryDataSourceImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val apiService: ApiService,
    private val breweryTypeMapper: BreweryTypeMapper,
    private val breweryMapper: BreweryMapper,
) : RemoteBreweryDataSource {
    override suspend fun getBrewery(
        id: String,
    ): Brewery = withContext(dispatchersProvider.io) {
        val breweryApi = apiService.getBrewery(id)
        return@withContext breweryMapper.mapToDomain(breweryApi)
    }

    override suspend fun getBreweries(
        ids: List<String>,
    ): List<Brewery> = withContext(dispatchersProvider.io) {
        val breweriesApi = apiService.getBreweries(ids.joinToString(","))
        return@withContext breweryMapper.mapToDomain(breweriesApi)
    }

    override suspend fun getBreweries(
        page: Int,
        pageSize: Int,
        type: BreweryType?,
    ): List<Brewery> = withContext(dispatchersProvider.io) {
        val breweriesApi = apiService.getBreweries(
            page = page,
            pageSize = pageSize,
            type = type?.let(breweryTypeMapper::mapFromDomain),
        )
        return@withContext breweryMapper.mapToDomain(breweriesApi)
    }
}
