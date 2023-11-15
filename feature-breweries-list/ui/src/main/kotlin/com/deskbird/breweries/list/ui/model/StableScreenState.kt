package com.deskbird.breweries.list.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.deskbird.breweries.list.BreweriesScreenState
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.strings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal data class StableBreweriesScreenState(
    val breweryTypes: ImmutableList<StableBreweryType>,
    val selectedType: StableBreweryType?,
    val breweries: ImmutableList<StableBreweryItem>,
    val progressIndicatorVisible: Boolean,
    val errorVisible: Boolean
)

@Composable
@ReadOnlyComposable
internal fun BreweriesScreenState.toStable() = StableBreweriesScreenState(
    breweryTypes = breweryTypes.map { it.toStable() }.toImmutableList(),
    selectedType = selectedType?.toStable(),
    breweries = breweries.map { it.toStable() }.toImmutableList(),
    progressIndicatorVisible = progressIndicatorVisible,
    errorVisible = errorVisible,
)

@Composable
@ReadOnlyComposable
private fun Brewery.toStable() = StableBreweryItem(
    id = id,
    name = name,
    city = address.city,
    country = address.country,
    breweryType = type.toStable(),
    isFavorite = isFavorite,
)

internal data class StableBreweryItem(
    val id: String,
    val name: String,
    val city: String,
    val country: String,
    val breweryType: StableBreweryType,
    val isFavorite: Boolean,
)

data class StableBreweryType(
    val index: Int,
    val name: String
)

@Composable
@ReadOnlyComposable
internal fun BreweryType.toStable() = StableBreweryType(
    index = this.ordinal,
    name = stringResource(
        id = when (this) {
            BreweryType.MICRO -> R.string.micro
            BreweryType.NANO -> R.string.nano
            BreweryType.REGIONAL -> R.string.regional
            BreweryType.BREWPUB -> R.string.brewpub
            BreweryType.LARGE -> R.string.large
            BreweryType.PLANNING -> R.string.planning
            BreweryType.BAR -> R.string.bar
            BreweryType.CONTRACT -> R.string.contract
            BreweryType.PROPRIETOR -> R.string.proprietor
            BreweryType.CLOSED -> R.string.closed
        }
    )
)
