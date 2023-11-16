package com.deskbird.breweries.details.ui

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.navigation.compose.hiltViewModel
import com.deskbird.breweries.details.BreweryDetailsEvent
import com.deskbird.breweries.details.BreweryDetailsViewModel
import com.deskbird.breweries.details.ui.model.StableBreweryDetailsScreenState
import com.deskbird.breweries.details.ui.model.toStable
import com.deskbird.breweries.details.ui.preview.BreweryDetailsScreenPreviewDataProvider
import com.deskbird.designsystem.theme.BrewzardThemeWithBackground
import com.deskbird.designsystem.util.DevicePreview
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
    onShowMessage: (String) -> Unit
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
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreweryDetailsScreenContent(
    state: StableBreweryDetailsScreenState,
    onCallClick: (String) -> Unit = {},
    onShowOnMapClick: (Float, Float) -> Unit = { _, _ -> },
    onOpenWebsiteClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorScheme.primaryContainer
            ),
            title = {
                state.brewery?.name?.let {
                    Text(text = it, color = colorScheme.onPrimaryContainer)
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_description)
                    )
                }
            }
        )
    }
}

@Composable
@DevicePreview
private fun Preview(
    @PreviewParameter(BreweryDetailsScreenPreviewDataProvider::class) data: StableBreweryDetailsScreenState,
) = BrewzardThemeWithBackground { BreweryDetailsScreenContent(state = data) }