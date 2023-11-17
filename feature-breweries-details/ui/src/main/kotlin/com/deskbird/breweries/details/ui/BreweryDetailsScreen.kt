package com.deskbird.breweries.details.ui

import android.Manifest
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deskbird.breweries.details.BreweryDetailsEvent
import com.deskbird.breweries.details.BreweryDetailsViewModel
import com.deskbird.breweries.details.ui.model.StableBrewery
import com.deskbird.breweries.details.ui.model.StableBreweryDetailsScreenState
import com.deskbird.breweries.details.ui.model.toStable
import com.deskbird.breweries.details.ui.preview.BreweryDetailsScreenPreviewDataProvider
import com.deskbird.designsystem.components.BrewzardButton
import com.deskbird.designsystem.components.BrewzardError
import com.deskbird.designsystem.theme.BrewzardThemeWithBackground
import com.deskbird.designsystem.util.DevicesPreview
import com.deskbird.strings.R
import com.deskbird.ui.util.ObserveAsEvents
import com.deskbird.ui.util.call
import com.deskbird.ui.util.openMap
import com.deskbird.ui.util.openWebsite
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BreweryDetailsScreen(
    onBackClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val viewModel = hiltViewModel<BreweryDetailsViewModel>()
    val state by viewModel.state.collectAsState()
    val stableState = state.toStable()

    val callPermissionState = rememberPermissionState(permission = Manifest.permission.CALL_PHONE)
    val context = LocalContext.current
    val callPermissionMissingMessage = stringResource(id = R.string.missing_phone_call_permission)
    val cannotCallMessage = stringResource(id = R.string.cannot_make_phone_call)
    val cannotOpenWebsiteMessage = stringResource(id = R.string.cannot_open_website)
    val cannotOpenMapMessage = stringResource(id = R.string.cannot_show_on_map)

    ObserveAsEvents(flow = viewModel.events) {
        when (it) {
            is BreweryDetailsEvent.Call -> {
                when {
                    callPermissionState.hasPermission -> {
                        if (!context.call(it.phoneNumber)) {
                            onShowMessage(cannotCallMessage)
                        }
                    }

                    callPermissionState.shouldShowRationale -> {
                        callPermissionState.launchPermissionRequest()
                    }

                    callPermissionState.permissionRequested -> {
                        onShowMessage(callPermissionMissingMessage)
                    }

                    else -> {
                        callPermissionState.launchPermissionRequest()
                    }
                }
            }

            is BreweryDetailsEvent.GoToWebsite -> {
                if (!context.openWebsite(it.url)) {
                    onShowMessage(cannotOpenWebsiteMessage)
                }
            }

            is BreweryDetailsEvent.ShowOnMap -> {
                if (!context.openMap(it.latitude, it.longitude)) {
                    onShowMessage(cannotOpenMapMessage)
                }
            }
        }
    }
    BreweryDetailsScreenContent(
        state = stableState,
        onCallClick = viewModel::onCallClick,
        onShowOnMapClick = viewModel::onShowOnMapClick,
        onOpenWebsiteClick = viewModel::onGotToWebsiteClick,
        onFavoriteClick = viewModel::onFavoriteButtonClick,
        onTryAgainClick = viewModel::onTryAgainClick,
        onBackClick = onBackClick,
    )
}

@Composable
private fun BreweryDetailsScreenContent(
    state: StableBreweryDetailsScreenState,
    onCallClick: (String) -> Unit = {},
    onShowOnMapClick: (Float, Float) -> Unit = { _, _ -> },
    onOpenWebsiteClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit = {},
    onTryAgainClick: () -> Unit = {},
) = Box(modifier = Modifier.fillMaxSize()) {
    Column {
        DetailsTopBar(
            title = state.brewery?.name,
            isFavorite = state.brewery?.isFavorite,
            onBackClick = onBackClick,
            onFavoriteClick = onFavoriteClick,
        )

        state.brewery?.let { brewery ->
            Details(brewery, onCallClick, onShowOnMapClick, onOpenWebsiteClick)
        }
    }

    if (state.progressIndicatorVisible) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }

    if (state.errorVisible) {
        BrewzardError(
            modifier = Modifier.align(Alignment.Center),
            message = stringResource(id = R.string.details_error_message),
            onTryAgainClick = onTryAgainClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsTopBar(
    title: String?,
    isFavorite: Boolean?,
    onBackClick: () -> Unit = {},
    onFavoriteClick: (Boolean) -> Unit,
) = TopAppBar(
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = colorScheme.primaryContainer,
        navigationIconContentColor = colorScheme.onPrimaryContainer,
        titleContentColor = colorScheme.onPrimaryContainer,
        actionIconContentColor = colorScheme.onPrimaryContainer,
    ),
    navigationIcon = {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back_description),
            )
        }
    },
    title = {
        title?.let {
            Text(
                text = it,
                overflow = TextOverflow.Ellipsis,
                style = typography.titleLarge,
                maxLines = 1,
            )
        }
    },
    actions = {
        isFavorite?.let {
            IconButton(onClick = { onFavoriteClick(it) }) {
                Icon(
                    if (it) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Outlined.FavoriteBorder
                    },
                    contentDescription = null,
                )
            }
        }
    },
)

@Composable
private fun Details(
    brewery: StableBrewery,
    onCallClick: (String) -> Unit = {},
    onShowOnMapClick: (Float, Float) -> Unit = { _, _ -> },
    onOpenWebsiteClick: (String) -> Unit = {},
) = Column(
    modifier = Modifier
        .padding(16.dp)
        .verticalScroll(rememberScrollState()),
    verticalArrangement = spacedBy(16.dp),
) {
    DetailsInfoSection(
        title = stringResource(id = R.string.brewery_type),
        details = "${brewery.type.name} - ${brewery.type.description}",
    )
    DetailsInfoSection(
        title = stringResource(id = R.string.address),
        details = listOfNotNull(
            brewery.address.street,
            brewery.address.city,
            brewery.address.stateProvince,
            brewery.address.postalCode,
        ).joinToString(", "),
        buttonText = brewery.coordinates?.let {
            stringResource(id = R.string.view_on_map)
        },
        onClick = {
            brewery.coordinates?.let {
                onShowOnMapClick(
                    it.latitude,
                    it.longitude,
                )
            }
        },
    )
    brewery.phone?.let { phone ->
        DetailsInfoSection(
            title = stringResource(id = R.string.phone),
            details = phone,
            buttonText = stringResource(id = R.string.call),
            onClick = {
                onCallClick(phone)
            },
        )
    }
    brewery.websiteUrl?.let { websiteUrl ->
        DetailsInfoSection(
            title = stringResource(id = R.string.website),
            details = websiteUrl,
            buttonText = stringResource(id = R.string.view_website),
            onClick = {
                onOpenWebsiteClick(websiteUrl)
            },
        )
    }
}

@Composable
private fun DetailsInfoSection(
    modifier: Modifier = Modifier,
    title: String,
    details: String,
    buttonText: String? = null,
    onClick: () -> Unit = {},
) = Column(modifier = modifier) {
    Text(text = title, style = typography.titleLarge, color = colorScheme.onBackground)
    Divider(
        Modifier.padding(top = 4.dp, bottom = 4.dp),
        thickness = 1.dp,
        color = colorScheme.onBackground,
    )
    Text(text = details, style = typography.bodyLarge, color = colorScheme.onBackground)
    buttonText?.let {
        BrewzardButton(
            modifier = Modifier
                .widthIn(min = 160.dp)
                .padding(top = 8.dp),
            onClick = onClick,
            text = buttonText,
        )
    }
}

@Composable
@DevicesPreview
private fun Preview(
    @PreviewParameter(BreweryDetailsScreenPreviewDataProvider::class) data: StableBreweryDetailsScreenState,
) = BrewzardThemeWithBackground { BreweryDetailsScreenContent(state = data) }