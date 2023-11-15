package com.deskbird.breweries.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.domain.repository.BreweriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BreweriesListViewModel @Inject constructor(
    private val breweriesRepository: BreweriesRepository,
) : ViewModel() {

    private val _navEvents = Channel<BreweriesScreenNavEvent>()
    val navEvents = _navEvents.receiveAsFlow()

    private val _screenState = MutableStateFlow(BreweriesScreenState())
    val screenState = _screenState.asStateFlow()

    private val pages = mutableMapOf<Int, List<Brewery>>()

    init {
        fetchBreweries(page = 1)
    }

    private fun fetchBreweries(page: Int, type: BreweryType? = null) {
        if (type != screenState.value.selectedType) {
            pages.clear()
        }
        viewModelScope.launch {
            _screenState.update {
                it.copy(
                    breweries = pages.flatMap { it.value },
                    progressIndicatorVisible = true
                )
            }

            try {
                val breweries = breweriesRepository.getBreweries(page, type)
                pages[page] = breweries
                _screenState.update {
                    it.copy(
                        selectedType = type,
                        breweries = pages.flatMap { it.value },
                        progressIndicatorVisible = false
                    )
                }
            } catch (exception: DataSourceException) {
                _screenState.update {
                    it.copy(
                        selectedType = type,
                        errorVisible = true,
                        progressIndicatorVisible = false
                    )
                }
            }
        }
    }

    fun onTypeSelected(type: BreweryType?) {
        if (type != screenState.value.selectedType) {
            fetchBreweries(page = 1, type = type)
        }
    }

    fun onBreweryClick(breweryId: String) {
        viewModelScope.launch {
            _navEvents.send(BreweriesScreenNavEvent.GoToDetails(breweryId))
        }
    }

    fun onFavoriteClick(breweryId: String) {
        viewModelScope.launch {
            screenState.value.breweries.find { it.id == breweryId }?.let { brewery ->
                if (brewery.isFavorite) {
                    breweriesRepository.removeFromFavorites(brewery)
                } else {
                    breweriesRepository.addToFavorites(brewery)
                }
                _screenState.update { oldState ->
                    oldState.copy(breweries = oldState.breweries.map {
                        if (it.id == breweryId) {
                            it.copy(isFavorite = !it.isFavorite)
                        } else {
                            it
                        }
                    })
                }
            }
        }
    }

    fun onItemVisible(position: Int) {
        if (position % 10 == PAGE_SIZE - 1) {
            val pageForPosition = position / PAGE_SIZE + 1
            val nextPage = pageForPosition + 1
            if (!pages.containsKey(nextPage)) {
                fetchBreweries(
                    page = (screenState.value.breweries.size / PAGE_SIZE) + 1,
                    type = screenState.value.selectedType
                )
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}