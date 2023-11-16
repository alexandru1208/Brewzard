package com.deskbird.breweries.details.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource
import com.deskbird.breweries.details.BreweryDetailsScreenState
import com.deskbird.domain.model.Address
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.domain.model.Coordinates
import com.deskbird.strings.R

internal data class StableBreweryDetailsScreenState(
    val brewery: StableBrewery? = null,
    val progressIndicatorVisible: Boolean = false,
    val errorVisible: Boolean = false,
)

@Composable
@ReadOnlyComposable
internal fun BreweryDetailsScreenState.toStable() = StableBreweryDetailsScreenState(
    brewery = brewery?.toStable(),
    progressIndicatorVisible = progressIndicatorVisible,
    errorVisible = errorVisible
)

internal data class StableBrewery(
    val name: String,
    val websiteUrl: String?,
    val phone: String?,
    val breweryType: String,
    val address: StableAddress,
    val coordinates: StableCoordinates?,
    val isFavorite: Boolean,
)

@Composable
@ReadOnlyComposable
private fun Brewery.toStable() = StableBrewery(
    name = name,
    websiteUrl = websiteUrl,
    phone = phone,
    breweryType = type.toStable(),
    address = address.toStable(),
    coordinates = coordinates?.toStable(),
    isFavorite = isFavorite
)

internal data class StableAddress(
    val street: String?,
    val city: String,
    val stateProvince: String,
    val state: String,
    val country: String,
    val postalCode: String,
)

private fun Address.toStable() = StableAddress(
    street = street,
    city = city,
    stateProvince = stateProvince,
    state = state,
    country = country,
    postalCode = postalCode
)

internal data class StableCoordinates(
    val latitude: Float,
    val longitude: Float
)

private fun Coordinates.toStable() = StableCoordinates(
    latitude = latitude,
    longitude = longitude
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
