package com.deskbird.brewzard.datasource.remote.internal

import com.deskbird.brewzard.datasource.remote.internal.model.BreweryApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ApiService {

    @GET("breweries/{id}")
    suspend fun getBrewery(
        @Path("id") id: String,
    ): BreweryApi

    @GET("breweries")
    suspend fun getBreweries(
        @Query("by_ids") ids: String,
    ): List<BreweryApi>

    @GET("breweries")
    suspend fun getBreweries(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
        @Query("by_type") type: String?,
    ): List<BreweryApi>
}
