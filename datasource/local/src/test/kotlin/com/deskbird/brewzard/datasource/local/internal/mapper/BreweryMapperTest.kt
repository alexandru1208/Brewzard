package com.deskbird.brewzard.datasource.local.internal.mapper

import com.deskbird.brewzard.datasource.local.internal.model.BreweryEntity
import com.deskbird.brewzard.domain.model.Address
import com.deskbird.brewzard.domain.model.Brewery
import com.deskbird.brewzard.domain.model.BreweryType
import com.deskbird.brewzard.domain.model.Coordinates
import io.mockk.every
import io.mockk.mockk
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test

internal class BreweryMapperTest {

    private val breweryTypeMapper = mockk<BreweryTypeMapper> {
        every { mapToDomain("brewpub") } returns BreweryType.BREWPUB
        every { mapFromDomain(BreweryType.BREWPUB) } returns "brewpub"
    }
    private val tested = BreweryMapper(breweryTypeMapper)

    @Test
    fun `should map brewery from domain correctly`() {
        val domainBrewery = Brewery(
            id = "Id",
            name = "Name",
            websiteUrl = "Website",
            phone = "Phone",
            type = BreweryType.BREWPUB,
            address = Address(
                street = "Street",
                city = "City",
                stateProvince = "StateProvince",
                state = "State",
                country = "Country",
                postalCode = "PostalCode",
            ),
            coordinates = Coordinates(0f, 0f),
            isFavorite = true,
        )

        tested.mapFromDomain(domainBrewery) `should be equal to` BreweryEntity(
            id = "Id",
            name = "Name",
            type = "brewpub",
            city = "City",
            stateProvince = "StateProvince",
            postalCode = "PostalCode",
            country = "Country",
            latitude = 0f,
            longitude = 0f,
            phone = "Phone",
            websiteUrl = "Website",
            street = "Street",
            state = "State",
        )
    }

    @Test
    fun `should map brewery to domain correctly`() {
        val breweryEntity = BreweryEntity(
            id = "Id",
            name = "Name",
            type = "brewpub",
            city = "City",
            stateProvince = "StateProvince",
            postalCode = "PostalCode",
            country = "Country",
            latitude = 0f,
            longitude = 0f,
            phone = "Phone",
            websiteUrl = "Website",
            street = "Street",
            state = "State",
        )

        tested.mapToDomain(breweryEntity) `should be equal to` Brewery(
            id = "Id",
            name = "Name",
            websiteUrl = "Website",
            phone = "Phone",
            type = BreweryType.BREWPUB,
            address = Address(
                street = "Street",
                city = "City",
                stateProvince = "StateProvince",
                state = "State",
                country = "Country",
                postalCode = "PostalCode",
            ),
            coordinates = Coordinates(0f, 0f),
            isFavorite = true,
        )
    }
}
