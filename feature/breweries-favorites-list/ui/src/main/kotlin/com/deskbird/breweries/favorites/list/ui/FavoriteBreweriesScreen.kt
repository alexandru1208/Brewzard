package com.deskbird.breweries.favorites.list.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deskbird.breweries.favorites.list.FavoriteBreweriesEvent
import com.deskbird.breweries.favorites.list.FavoriteBreweriesViewModel
import com.deskbird.breweries.favorites.list.ui.model.StableFavoriteBreweriesScreenState
import com.deskbird.breweries.favorites.list.ui.model.toStable
import com.deskbird.breweries.favorites.list.ui.preview.FavoriteBreweriesScreenPreviewDataProvider
import com.deskbird.designsystem.components.BreweryCard
import com.deskbird.designsystem.theme.BrewzardThemeWithBackground
import com.deskbird.designsystem.util.DevicesPreview
import com.deskbird.strings.R
import com.deskbird.ui.util.ObserveAsEvents

@Composable
fun FavoriteBreweriesScreen(onNavigateToDetails: (String) -> Unit) {
    val viewModel = hiltViewModel<FavoriteBreweriesViewModel>()
    val state by viewModel.state.collectAsState()
    val stableState = state.toStable()
    ObserveAsEvents(flow = viewModel.events) {
        when (it) {
            is FavoriteBreweriesEvent.GoToDetails -> onNavigateToDetails(it.breweryId)
        }
    }
    BreweriesScreenContent(
        state = stableState,
        onBreweryClick = viewModel::onBreweryClick,
        onFavoriteClick = viewModel::onFavoriteClick,
    )
}

@Composable
private fun BreweriesScreenContent(
    state: StableFavoriteBreweriesScreenState,
    onBreweryClick: (String) -> Unit = {},
    onFavoriteClick: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp),
    ) {
        LazyColumn(
            verticalArrangement = spacedBy(16.dp),
        ) {
            state.breweries.forEach { brewery ->
                item(key = brewery.id) {
                    BreweryCard(
                        name = brewery.name,
                        city = brewery.city,
                        country = brewery.country,
                        breweryType = brewery.breweryType,
                        isFavorite = brewery.isFavorite,
                        onFavoriteClick = { onFavoriteClick(brewery.id) },
                        onClick = { onBreweryClick(brewery.id) },
                    )
                }
            }
        }
        if (state.progressIndicatorVisible) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        if (state.breweries.isEmpty()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.no_favorites_message),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
@DevicesPreview
private fun Preview(
    @PreviewParameter(FavoriteBreweriesScreenPreviewDataProvider::class)
    data: StableFavoriteBreweriesScreenState,
) = BrewzardThemeWithBackground { BreweriesScreenContent(state = data) }
