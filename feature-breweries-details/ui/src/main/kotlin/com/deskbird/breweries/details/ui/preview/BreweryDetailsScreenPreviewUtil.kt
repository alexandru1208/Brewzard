package com.deskbird.breweries.details.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.deskbird.breweries.details.ui.model.StableAddress
import com.deskbird.breweries.details.ui.model.StableBrewery
import com.deskbird.breweries.details.ui.model.StableBreweryDetailsScreenState
import com.deskbird.breweries.details.ui.model.StableCoordinates

private fun createUiState(
    isFavorite: Boolean = false,
    progressVisible: Boolean = false,
    errorVisible: Boolean = false,
) = StableBreweryDetailsScreenState(
    brewery = createPreviewBrewery(isFavorite),
    progressIndicatorVisible = progressVisible,
    errorVisible = errorVisible,
)

private fun createPreviewBrewery(isFavorite: Boolean) = StableBrewery(
    name = "(405) Brewing Co",
    websiteUrl = "http://www.405brewing.com",
    phone = "4058160490",
    breweryType = "Micro",
    address = StableAddress(
        street = "1716 Topeka St",
        city = "Norman",
        stateProvince = "Oklahoma",
        country = "United States",
        state = "Oklahoma",
        postalCode = "73069-8224"
    ),
    coordinates = StableCoordinates(
        latitude = 35.25739f,
        longitude = -97.468185f
    ),
    isFavorite = isFavorite
)

internal class BreweryDetailsScreenPreviewDataProvider :
    PreviewParameterProvider<StableBreweryDetailsScreenState> {
    override val values = sequenceOf(
        createUiState(),
        createUiState(isFavorite = true),
        createUiState(progressVisible = true),
        createUiState(errorVisible = true),
    )
}
