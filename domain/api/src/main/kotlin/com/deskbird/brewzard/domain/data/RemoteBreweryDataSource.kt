package com.deskbird.brewzard.domain.data

import com.deskbird.brewzard.domain.model.Brewery
import com.deskbird.brewzard.domain.model.BreweryType

interface RemoteBreweryDataSource {

    suspend fun getBrewery(id: String): Brewery

    suspend fun getBreweries(ids: List<String>): List<Brewery>

    suspend fun getBreweries(page: Int, pageSize: Int, type: BreweryType? = null): List<Brewery>
}
