package com.deskbird.breweries.list.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.deskbird.breweries.list.ui.model.StableBreweriesScreenState
import com.deskbird.breweries.list.ui.model.StableBreweryItem
import com.deskbird.breweries.list.ui.model.StableBreweryType
import kotlinx.collections.immutable.toImmutableList
import kotlin.random.Random.Default.nextBoolean

private fun createPreviewBreweries(count: Int) = (1..count).map {
    StableBreweryItem(
        id = "brewery$it",
        name = "Brewery $it",
        breweryType = "Type $it",
        country = "Country $it",
        city = "City $it",
        isFavorite = nextBoolean()

    )
}.toImmutableList()

private fun createUiState(
    progressVisible: Boolean = false,
    errorVisible: Boolean = false,
    nrOfBreweries: Int = 10,
    nrOfTypes: Int = 5,
    selectedTypeIndex: Int? = null,
) = StableBreweriesScreenState(
    breweryTypes = (1..nrOfTypes).map {
        StableBreweryType(it, "Type $it")
    }.toImmutableList(),
    selectedType = selectedTypeIndex?.let { StableBreweryType(it, "Type $it") },
    breweries = createPreviewBreweries(nrOfBreweries),
    progressIndicatorVisible = progressVisible,
    errorVisible = errorVisible,
)

internal class BreweriesScreenPreviewDataProvider :
    PreviewParameterProvider<StableBreweriesScreenState> {
    override val values = sequenceOf(
        createUiState(),
        createUiState(progressVisible = true),
        createUiState(errorVisible = true),
    )
}
