package com.deskbird.breweries.favorites.list

import app.cash.turbine.turbineScope
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.repository.BreweryRepository
import com.deskbird.test.data.TestBreweryFactory.createBrewery
import com.deskbird.test.util.CoroutinesTestExtension
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
class FavoriteBreweriesViewModelTest {

    private val favoritesFlow = MutableSharedFlow<List<Brewery>>()
    private val repository = mockk<BreweryRepository>(relaxed = true) {
        every { observeFavorites() } returns favoritesFlow
    }

    private val tested by lazy {
        FavoriteBreweriesViewModel(repository)
    }

    @Test
    fun `when initialized favorites are observed`() = runTest {
        // when
        tested
        advanceUntilIdle()

        // then
        verify { repository.observeFavorites() }
    }

    @Test
    fun `when initialized ui has correct state`() {
        // when
        tested

        // then
        tested.state.value `should be equal to` FavoriteBreweriesScreenState(
            breweries = emptyList(),
            progressIndicatorVisible = true,
        )
    }

    @Test
    fun `given initialized when favorites are emitted then ui state is correctly updated`() =
        runTest {
            // given
            val breweries = (1..4).map { createBrewery("breweryId$it", isFavorite = true) }
            tested
            advanceUntilIdle()

            // when
            favoritesFlow.emit(breweries)

            // then
            tested.state.value `should be equal to` FavoriteBreweriesScreenState(
                breweries = breweries,
                progressIndicatorVisible = false,
            )
        }

    @Test
    fun `given initialized and breweries displayed when brewery clicked then GoToDetails event is emitted`() =
        runTest {
            turbineScope {
                // given
                val breweries = (1..4).map { createBrewery("breweryId$it", isFavorite = true) }
                val clickedId = breweries.random().id
                val eventObserver = tested.events.testIn(this)
                advanceUntilIdle()
                favoritesFlow.emit(breweries)
                advanceUntilIdle()

                // when
                tested.onBreweryClick(clickedId)

                // then
                eventObserver.awaitItem() `should be equal to` FavoriteBreweriesEvent.GoToDetails(
                    clickedId,
                )
                eventObserver.cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given initialized and breweries displayed when favorite button clicked then remove from favorites`() =
        runTest {
            // given
            val breweries = (1..4).map { createBrewery("breweryId$it", isFavorite = true) }
            val clickedId = breweries.random().id
            val clickedBrewery = breweries.first { it.id == clickedId }
            advanceUntilIdle()
            favoritesFlow.emit(breweries)
            advanceUntilIdle()

            // when
            tested.onFavoriteClick(clickedId)

            // then
            coEvery { repository.removeFromFavorites(clickedBrewery) }
        }
}
