package com.deskbird.breweries.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.model.BreweryType
import com.deskbird.domain.repository.BreweryRepository
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
    private val breweryRepository: BreweryRepository,
) : ViewModel() {

    private val _events = Channel<BreweriesListEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(BreweriesScreenState())
    val state = _state.asStateFlow()

    private val pages = mutableMapOf<Int, List<Brewery>>()

    init {
        fetchBreweries(page = FIRST_PAGE_NUMBER)
    }

    private fun fetchBreweries(page: Int, type: BreweryType? = null) {
        if (type != state.value.selectedType) {
            pages.clear()
        }
        viewModelScope.launch {
            _state.update {
                it.copy(
                    breweries = pages.flatMap { it.value },
                    progressIndicatorVisible = true
                )
            }

            try {
                val breweries = breweryRepository.getBreweries(page, PAGE_SIZE, type)
                pages[page] = breweries
                _state.update {
                    it.copy(
                        selectedType = type,
                        breweries = pages.flatMap { it.value },
                        progressIndicatorVisible = false
                    )
                }
            } catch (exception: DataSourceException) {
                _state.update {
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
        if (type != state.value.selectedType) {
            fetchBreweries(page = FIRST_PAGE_NUMBER, type = type)
        }
    }

    fun onBreweryClick(breweryId: String) {
        viewModelScope.launch {
            _events.send(BreweriesListEvent.GoToDetails(breweryId))
        }
    }

    fun onFavoriteClick(breweryId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            state.value.breweries.find { it.id == breweryId }?.let { brewery ->
                if (isFavorite) {
                    breweryRepository.removeFromFavorites(brewery)
                } else {
                    breweryRepository.addToFavorites(brewery)
                }
                _state.update { oldState ->
                    oldState.copy(breweries = oldState.breweries.map {
                        if (it.id == breweryId) {
                            it.copy(isFavorite = !isFavorite)
                        } else {
                            it
                        }
                    })
                }
            }
        }
    }

    fun onItemVisible(position: Int) {
        if (position % PAGE_SIZE == PAGE_SIZE - 1) {
            val pageForPosition = position / PAGE_SIZE + 1
            val nextPage = pageForPosition + 1
            if (!pages.containsKey(nextPage)) {
                fetchBreweries(
                    page = (state.value.breweries.size / PAGE_SIZE) + 1,
                    type = state.value.selectedType
                )
            }
        }
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val FIRST_PAGE_NUMBER = 1
    }
}