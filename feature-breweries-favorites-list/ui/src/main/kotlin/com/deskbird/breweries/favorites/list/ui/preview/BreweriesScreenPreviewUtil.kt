package com.deskbird.breweries.favorites.list.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.deskbird.breweries.favorites.list.ui.model.StableFavoriteBreweriesScreenState
import com.deskbird.breweries.favorites.list.ui.model.StableBreweryItem
import com.deskbird.breweries.favorites.list.ui.model.StableBreweryType
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random.Default.nextBoolean

private fun createPreviewBreweries(count: Int) = (1..count).map {
    StableBreweryItem(
        id = "brewery$it",
        name = "Brewery $it",
        breweryType = StableBreweryType(it, "Type $it"),
        country = "Country $it",
        city = "City $it",
        isFavorite = nextBoolean()

    )
}.toImmutableList()

private fun createPreviewPickerUiState(
    progressVisible: Boolean = false,
    errorVisible: Boolean = false,
    nrOfBreweries: Int = 10,
) = StableFavoriteBreweriesScreenState(
    breweries = createPreviewBreweries(nrOfBreweries),
    progressIndicatorVisible = progressVisible,
    errorVisible = errorVisible,
)

internal class FavoriteBreweriesScreenPreviewDataProvider :
    PreviewParameterProvider<StableFavoriteBreweriesScreenState> {
    override val values = sequenceOf(
        createPreviewPickerUiState(),
        createPreviewPickerUiState(progressVisible = true),
        createPreviewPickerUiState(errorVisible = true),
    )
}
