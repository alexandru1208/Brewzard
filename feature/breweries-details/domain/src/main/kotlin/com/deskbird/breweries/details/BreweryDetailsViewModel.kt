package com.deskbird.breweries.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deskbird.domain.di.BreweryId
import com.deskbird.domain.error.DataSourceException
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
class BreweryDetailsViewModel @Inject constructor(
    @BreweryId val breweryId: String,
    private val breweryRepository: BreweryRepository,
) : ViewModel() {

    private val _events = Channel<BreweryDetailsEvent>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(BreweryDetailsScreenState())
    val state = _state.asStateFlow()

    init {
        fetchDetails()
    }

    private fun fetchDetails() {
        _state.update {
            it.copy(
                progressIndicatorVisible = true,
                errorVisible = false,
            )
        }
        viewModelScope.launch {
            try {
                val brewery = breweryRepository.getBrewery(breweryId)
                _state.update {
                    it.copy(
                        brewery = brewery,
                        progressIndicatorVisible = false,
                    )
                }
            } catch (exception: DataSourceException) {
                _state.update {
                    it.copy(
                        progressIndicatorVisible = false,
                        errorVisible = true,
                    )
                }
            }
        }
    }

    fun onCallClick(phoneNumber: String) {
        viewModelScope.launch {
            _events.send(BreweryDetailsEvent.Call(phoneNumber))
        }
    }

    fun onGotToWebsiteClick(url: String) {
        viewModelScope.launch {
            _events.send(BreweryDetailsEvent.GoToWebsite(url))
        }
    }

    fun onShowOnMapClick(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            _events.send(BreweryDetailsEvent.ShowOnMap(latitude, longitude))
        }
    }

    fun onFavoriteButtonClick(isFavorite: Boolean) {
        viewModelScope.launch {
            state.value.brewery?.let {
                if (isFavorite) {
                    breweryRepository.removeFromFavorites(it)
                } else {
                    breweryRepository.addToFavorites(it)
                }
            }
            _state.update {
                it.copy(brewery = it.brewery?.copy(isFavorite = !isFavorite))
            }
        }
    }

    fun onTryAgainClick() {
        fetchDetails()
    }
}
