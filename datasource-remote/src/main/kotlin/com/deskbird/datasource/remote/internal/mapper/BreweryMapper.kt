package com.deskbird.datasource.remote.internal.mapper

import com.deskbird.datasource.remote.internal.errors.NetworkExceptionMapper
import com.deskbird.datasource.remote.internal.model.BreweryApi
import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.error.RemoteError
import com.deskbird.domain.model.Address
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.Coordinates
import javax.inject.Inject

internal class BreweryMapper @Inject constructor(
    private val breweryTypeMapper: BreweryTypeMapper
) {

    fun mapToDomain(breweries: List<BreweryApi>): List<Brewery> = breweries.map(::mapToDomain)

    fun mapToDomain(brewery: BreweryApi): Brewery = with(brewery) {
        Brewery(
            id = id,
            name = name,
            websiteUrl = websiteUrl,
            phone = phone,
            type = breweryTypeMapper.mapToDomain(type),
            address = Address(
                street = street,
                city = city,
                stateProvince = stateProvince,
                state = state,
                country = country,
                postalCode = postalCode
            ),
            coordinates = try {
                latitude?.let { lat ->
                    longitude?.let { long ->
                        Coordinates(
                            latitude = lat.toFloat(),
                            longitude = long.toFloat()
                        )
                    }
                }
            } catch (exception: NumberFormatException) {
                throw DataSourceException(
                    RemoteError.MalformedDataError(exception)
                )
            },
            isFavorite = false,
        )
    }

}