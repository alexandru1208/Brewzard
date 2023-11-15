package com.deskbird.breweries.favorites.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class FavoriteBreweriesViewModel @Inject constructor(
    private val breweriesRepository: BreweriesRepository,
) : ViewModel() {

    private val _navEvents = Channel<FavoriteBreweriesScreenNavEvent>()
    val navEvents = _navEvents.receiveAsFlow()

    private val _screenState = MutableStateFlow(FavoriteBreweriesScreenState())
    val screenState = _screenState.asStateFlow()

    init {
        fetchFavorites()
    }

    private fun fetchFavorites() {
        viewModelScope.launch {
            _screenState.update {
                it.copy(
                    progressIndicatorVisible = true
                )
            }
            val breweries = breweriesRepository.getFavorites()
            _screenState.update {
                it.copy(
                    breweries = breweries,
                    progressIndicatorVisible = false
                )
            }
        }
    }

    fun onBreweryClick(breweryId: String) {
        viewModelScope.launch {
            _navEvents.send(FavoriteBreweriesScreenNavEvent.GoToDetails(breweryId))
        }
    }

    fun onFavoriteClick(breweryId: String) {
        viewModelScope.launch {
            screenState.value.breweries.find { it.id == breweryId }?.let { brewery ->
                breweriesRepository.removeFromFavorites(brewery)
                _screenState.update { oldState ->
                    oldState.copy(breweries = oldState.breweries.filter { it.id != breweryId })
                }
            }
        }
    }

}