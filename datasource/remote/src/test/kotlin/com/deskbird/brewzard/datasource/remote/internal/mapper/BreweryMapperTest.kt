package com.deskbird.brewzard.datasource.remote.internal.mapper

import com.deskbird.brewzard.datasource.remote.internal.model.BreweryApi
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
        every { mapToDomain(any()) } returns BreweryType.BREWPUB
    }
    private val tested = BreweryMapper(breweryTypeMapper)

    @Test
    fun `should map brewery correctly`() {
        val breweryApi = BreweryApi(
            id = "Id",
            name = "Name",
            type = "large",
            city = "City",
            stateProvince = "StateProvince",
            postalCode = "PostalCode",
            country = "Country",
            latitude = "0",
            longitude = "0",
            phone = "Phone",
            websiteUrl = "Website",
            street = "Street",
            state = "State",
        )

        tested.mapToDomain(breweryApi) `should be equal to` Brewery(
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
            isFavorite = false,
        )
    }
}
