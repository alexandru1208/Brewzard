package com.deskbird.brewzard.datasource.local.internal.mapper

import com.deskbird.brewzard.datasource.local.internal.model.BreweryEntity
import com.deskbird.brewzard.domain.model.Address
import com.deskbird.brewzard.domain.model.Brewery
import com.deskbird.brewzard.domain.model.Coordinates
import javax.inject.Inject

internal class BreweryMapper @Inject constructor(
    private val breweryTypeMapper: BreweryTypeMapper,
) {

    fun mapFromDomain(breweries: List<Brewery>): List<BreweryEntity> =
        breweries.map(::mapFromDomain)

    fun mapFromDomain(brewery: Brewery): BreweryEntity = with(brewery) {
        BreweryEntity(
            id = id,
            name = name,
            type = breweryTypeMapper.mapFromDomain(type),
            city = address.city,
            state = address.state,
            stateProvince = address.stateProvince,
            street = address.street,
            postalCode = address.postalCode,
            country = address.country,
            latitude = coordinates?.latitude,
            longitude = coordinates?.longitude,
            websiteUrl = websiteUrl,
            phone = phone,
        )
    }

    fun mapToDomain(breweries: List<BreweryEntity>): List<Brewery> =
        breweries.map(::mapToDomain)

    fun mapToDomain(brewery: BreweryEntity): Brewery = with(brewery) {
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
                postalCode = postalCode,
            ),
            coordinates = latitude?.let { lat ->
                longitude?.let { long ->
                    Coordinates(
                        latitude = lat,
                        longitude = long,
                    )
                }
            },
            isFavorite = true,
        )
    }
}
