package com.deskbird.test.data

import com.deskbird.domain.model.Address
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.domain.model.Coordinates

object TestBreweryFactory {
    fun createBrewery(
        id: String = "Id",
        name: String = "Name",
        type: BreweryType = BreweryType.LARGE,
        city: String = "City",
        stateProvince: String = "StateProvince",
        postalCode: String = "PostalCode",
        country: String = "Country",
        latitude: Float = 0F,
        longitude: Float = 0F,
        phone: String = "Phone",
        websiteUrl: String = "Website",
        street: String = "Street",
        state: String = "State",
        isFavorite: Boolean = false,
    ) = Brewery(
        id = id,
        name = name,
        type = type,
        address = Address(street, city, stateProvince, state, country, postalCode),
        coordinates = Coordinates(latitude, longitude),
        phone = phone,
        websiteUrl = websiteUrl,
        isFavorite = isFavorite,
    )
}
