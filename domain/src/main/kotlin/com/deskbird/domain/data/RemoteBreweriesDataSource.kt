package com.deskbird.domain.data

import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType

interface RemoteBreweriesDataSource {

    suspend fun getBrewery(id: String): Brewery

    suspend fun getBreweries(ids: List<String>): List<Brewery>

    suspend fun getBreweries(page: Int, pageSize: Int, type: BreweryType? = null): List<Brewery>
}