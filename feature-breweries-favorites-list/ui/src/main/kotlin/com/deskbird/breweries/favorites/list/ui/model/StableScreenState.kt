package com.deskbird.breweries.favorites.list.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.deskbird.breweries.favorites.list.FavoriteBreweriesScreenState
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.strings.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

internal data class StableFavoriteBreweriesScreenState(
    val breweries: ImmutableList<StableBreweryItem>,
    val progressIndicatorVisible: Boolean,
    val errorVisible: Boolean
)

@Composable
@ReadOnlyComposable
internal fun FavoriteBreweriesScreenState.toStable() = StableFavoriteBreweriesScreenState(
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
    val breweryType: String,
    val isFavorite: Boolean,
)

data class StableBreweryType(
    val index: Int,
    val name: String
)

@Composable
@ReadOnlyComposable
private fun BreweryType.toStable() = stringResource(
    id = when (this) {
        BreweryType.MICRO -> R.string.micro_type
        BreweryType.NANO -> R.string.nano_type
        BreweryType.REGIONAL -> R.string.regional_type
        BreweryType.BREWPUB -> R.string.brewpub_type
        BreweryType.LARGE -> R.string.large_type
        BreweryType.PLANNING -> R.string.planning_type
        BreweryType.BAR -> R.string.bar_type
        BreweryType.CONTRACT -> R.string.contract_type
        BreweryType.PROPRIETOR -> R.string.proprietor_type
        BreweryType.CLOSED -> R.string.closed_type
    }
)
