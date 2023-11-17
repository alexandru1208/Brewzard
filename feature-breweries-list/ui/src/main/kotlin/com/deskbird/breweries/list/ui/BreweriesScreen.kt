package com.deskbird.breweries.list.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.deskbird.breweries.list.BreweriesListViewModel
import com.deskbird.breweries.list.BreweriesListEvent
import com.deskbird.breweries.list.ui.model.StableBreweriesScreenState
import com.deskbird.breweries.list.ui.model.StableBreweryType
import com.deskbird.breweries.list.ui.model.toStable
import com.deskbird.breweries.list.ui.preview.BreweriesScreenPreviewDataProvider
import com.deskbird.designsystem.components.BreweryCard
import com.deskbird.designsystem.components.BrewzardError
import com.deskbird.designsystem.theme.BrewzardThemeWithBackground
import com.deskbird.designsystem.util.DevicePreview
import com.deskbird.domain.model.BreweryType
import com.deskbird.strings.R
import com.deskbird.ui.util.ObserveAsEvents
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun BreweriesScreen(
    onNavigateToDetails: (String) -> Unit,
    onShowMessage: (String) -> Unit
) {
    val viewModel = hiltViewModel<BreweriesListViewModel>()
    val state by viewModel.state.collectAsState()
    val stableState = state.toStable()
    val fetchMoreError = stringResource(id = R.string.fetch_more_breweries_error_message)
    ObserveAsEvents(flow = viewModel.events) {
        when (it) {
            is BreweriesListEvent.GoToDetails -> onNavigateToDetails(it.breweryId)
            BreweriesListEvent.ShowFetchMoreError -> onShowMessage(fetchMoreError)
        }
    }
    BreweriesScreenContent(
        state = stableState,
        onBreweryClick = viewModel::onBreweryClick,
        onFavoriteClick = viewModel::onFavoriteClick,
        onTypeSelected = { viewModel.onTypeSelected(it?.index?.let(BreweryType.entries::get)) },
        onItemVisible = viewModel::onItemVisible,
        onTryAgainClick = viewModel::onTryAgainClick
    )
}

@Composable
private fun BreweriesScreenContent(
    state: StableBreweriesScreenState,
    onBreweryClick: (String) -> Unit = {},
    onFavoriteClick: (String, Boolean) -> Unit = { _, _ -> },
    onTypeSelected: (StableBreweryType?) -> Unit = {},
    onItemVisible: (Int) -> Unit = {},
    onTryAgainClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = spacedBy(16.dp),
        ) {
            BreweryTypePicker(
                modifier = Modifier.fillMaxWidth(),
                types = state.breweryTypes,
                selectedType = state.selectedType,
                onTypeSelected = onTypeSelected
            )

            val listState = rememberLazyListState()

            LazyColumn(
                verticalArrangement = spacedBy(16.dp),
                state = listState,
            ) {
                state.breweries.forEach { brewery ->
                    item(key = brewery.id) {
                        BreweryCard(name = brewery.name,
                            city = brewery.city,
                            country = brewery.country,
                            breweryType = brewery.breweryType,
                            isFavorite = brewery.isFavorite,
                            onFavoriteClick = { onFavoriteClick(brewery.id, brewery.isFavorite) },
                            onClick = { onBreweryClick(brewery.id) })
                    }
                }
            }

            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .filterNotNull()
                    .distinctUntilChanged()
                    .collect(onItemVisible)
            }
        }

        if (state.progressIndicatorVisible) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        if (state.errorVisible) {
            BrewzardError(
                modifier = Modifier.align(Alignment.Center),
                message = stringResource(id = R.string.fetch_breweries_error_message),
                onTryAgainClick = onTryAgainClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BreweryTypePicker(
    modifier: Modifier = Modifier,
    types: ImmutableList<StableBreweryType>,
    selectedType: StableBreweryType?,
    onTypeSelected: (StableBreweryType?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(16.dp)
    ) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }

        Text(text = "${stringResource(id = R.string.brewery_type)}:")
        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
        ) {
            TextField(
                value = selectedType?.name ?: stringResource(id = R.string.all_types),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.all_types)) },
                    onClick = {
                        onTypeSelected(null)
                        isExpanded = false
                    }
                )
                types.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            onTypeSelected(it)
                            isExpanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
@DevicePreview
private fun Preview(
    @PreviewParameter(BreweriesScreenPreviewDataProvider::class) data: StableBreweriesScreenState,
) = BrewzardThemeWithBackground { BreweriesScreenContent(state = data) }