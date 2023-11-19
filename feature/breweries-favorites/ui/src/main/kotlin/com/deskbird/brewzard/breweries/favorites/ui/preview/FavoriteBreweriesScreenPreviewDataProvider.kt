package com.deskbird.brewzard.breweries.favorites.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.deskbird.brewzard.breweries.favorites.ui.model.StableBreweryItem
import com.deskbird.brewzard.breweries.favorites.ui.model.StableFavoriteBreweriesScreenState
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random.Default.nextBoolean

private fun createPreviewBreweries(count: Int) = (1..count).map {
    StableBreweryItem(
        id = "brewery$it",
        name = "Brewery $it",
        breweryType = "Type $it",
        country = "Country $it",
        city = "City $it",
        isFavorite = nextBoolean(),

    )
}.toImmutableList()

private fun createUiState(
    progressVisible: Boolean = false,
    nrOfBreweries: Int = 10,
) = StableFavoriteBreweriesScreenState(
    breweries = createPreviewBreweries(nrOfBreweries),
    progressIndicatorVisible = progressVisible,
)

internal class FavoriteBreweriesScreenPreviewDataProvider :
    PreviewParameterProvider<StableFavoriteBreweriesScreenState> {
    override val values = sequenceOf(
        createUiState(),
        createUiState(progressVisible = true),
    )
}
