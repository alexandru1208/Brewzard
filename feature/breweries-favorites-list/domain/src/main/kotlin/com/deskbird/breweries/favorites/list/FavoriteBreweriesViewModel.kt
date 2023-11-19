package com.deskbird.breweries.favorites.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class FavoriteBreweriesViewModel @Inject constructor(
    private val breweryRepository: BreweryRepository,
) : ViewModel() {

    private val _events = Channel<FavoriteBreweriesEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(FavoriteBreweriesScreenState())
    val state = _state.asStateFlow()

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        _state.update {
            it.copy(
                progressIndicatorVisible = true,
            )
        }
        viewModelScope.launch {
            breweryRepository.observeFavorites().collect { breweries ->
                _state.update {
                    it.copy(
                        breweries = breweries,
                        progressIndicatorVisible = false,
                    )
                }
            }
        }
    }

    fun onBreweryClick(breweryId: String) {
        viewModelScope.launch {
            _events.send(FavoriteBreweriesEvent.GoToDetails(breweryId))
        }
    }

    fun onFavoriteClick(breweryId: String) {
        viewModelScope.launch {
            state.value.breweries.find { it.id == breweryId }?.let { brewery ->
                breweryRepository.removeFromFavorites(brewery)
            }
        }
    }
}
