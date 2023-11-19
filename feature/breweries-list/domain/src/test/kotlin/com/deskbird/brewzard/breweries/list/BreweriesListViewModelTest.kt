package com.deskbird.brewzard.breweries.list

import app.cash.turbine.turbineScope
import com.deskbird.brewzard.domain.error.DataSourceException
import com.deskbird.brewzard.domain.model.Brewery
import com.deskbird.brewzard.domain.model.BreweryType
import com.deskbird.brewzard.domain.repository.BreweryRepository
import com.deskbird.brewzard.test.data.TestBreweryFactory.createBrewery
import com.deskbird.brewzard.test.util.CoroutinesTestExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val PAGE_SIZE = 10

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
class BreweriesListViewModelTest {

    private val favoritesFlow = MutableSharedFlow<List<Brewery>>()
    private val repository = mockk<BreweryRepository>(relaxed = true) {
        every { observeFavorites() } returns favoritesFlow
    }

    private val tested by lazy {
        BreweriesListViewModel(repository)
    }

    @Test
    fun `when initialized then start observing favorite`() = runTest {
        // when
        tested
        advanceUntilIdle()

        // then
        coVerify { repository.observeFavorites() }
    }

    @Test
    fun `when initialized then fetch first page without type filtering`() = runTest {
        // when
        tested
        advanceUntilIdle()

        // then
        coVerify { repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null) }
    }

    @Test
    fun `given initialized when type selected then fetch first page with selected type`() =
        runTest {
            // given
            val type = BreweryType.LARGE
            tested
            advanceUntilIdle()

            // when
            tested.onTypeSelected(type)
            advanceUntilIdle()

            // then
            coVerify { repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = type) }
        }

    @Test
    fun `when initialized then ui has correct state`() = runTest {
        // when
        tested

        // then
        tested.state.value `should be equal to` BreweriesScreenState(
            breweryTypes = BreweryType.entries,
            selectedType = null,
            breweries = emptyList(),
            progressIndicatorVisible = true,
            errorVisible = false,
        )
    }

    @Test
    fun `given breweries are returned from repository when initialized then ui has correct state`() =
        runTest {
            // given
            val breweries = (1..PAGE_SIZE).map { createBrewery("breweryId$it") }
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
            } returns breweries

            // when
            tested
            advanceUntilIdle()

            // then
            tested.state.value `should be equal to` BreweriesScreenState(
                breweryTypes = BreweryType.entries,
                selectedType = null,
                breweries = breweries,
                progressIndicatorVisible = false,
                errorVisible = false,
            )
        }

    @Test
    fun `given type was selected but fetch failed  when try again clicked then first page is requested again with selected type`() =
        runTest {
            // given
            val type = BreweryType.BREWPUB
            val breweries = (1..PAGE_SIZE).map { createBrewery("breweryId$it") }
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = type)
            } throws mockk<DataSourceException>() andThen breweries
            tested.onTypeSelected(type)

            // when
            tested.onTryAgainClick()
            advanceUntilIdle()

            // then
            coVerify(exactly = 2) { repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type) }
        }

    @Test
    fun `given first page was fetched when last item from page is visible then next page is requested`() =
        runTest {
            // given
            val breweries = (1..PAGE_SIZE).map { createBrewery("breweryId$it") }
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
            } returns breweries
            tested
            advanceUntilIdle()

            // when
            tested.onItemVisible(PAGE_SIZE - 1)
            advanceUntilIdle()

            // then
            coVerify { repository.getBreweries(page = 2, pageSize = PAGE_SIZE, null) }
        }

    @Test
    fun `given breweries request throws when initialized then then ui has correct state`() =
        runTest {
            // given
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
            } throws mockk<DataSourceException>()

            // when
            tested
            advanceUntilIdle()

            // then
            tested.state.value `should be equal to` BreweriesScreenState(
                breweryTypes = BreweryType.entries,
                selectedType = null,
                breweries = emptyList(),
                progressIndicatorVisible = false,
                errorVisible = true,
            )
        }

    @Test
    fun `given initialized and breweries displayed when favorites emitted then ui has correct state`() =
        runTest {
            // given
            val ids = (1..PAGE_SIZE).map { "breweryId$it" }.toSet()
            val favoriteIds = ids.asSequence().shuffled().take(5).toSet()
            val breweries = ids.map { createBrewery(id = it) }
            val favoriteBreweries = favoriteIds.map { createBrewery(id = it) }
            val updatedBreweries =
                breweries.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
            } returns breweries
            tested
            advanceUntilIdle()

            // when
            favoritesFlow.emit(favoriteBreweries)

            // then
            tested.state.value `should be equal to` BreweriesScreenState(
                breweryTypes = BreweryType.entries,
                selectedType = null,
                breweries = updatedBreweries,
                progressIndicatorVisible = false,
                errorVisible = false,
            )
        }

    @Test
    fun `given initialized and breweries displayed when brewery clicked then GoToDetails event is emitted`() =
        runTest {
            turbineScope {
                // given
                val eventObserver = tested.events.testIn(this)
                val ids = (1..PAGE_SIZE).map { "breweryId$it" }.toSet()
                val clickedId = ids.random()
                val breweries = ids.map { createBrewery(id = it) }
                coEvery {
                    repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
                } returns breweries
                tested
                advanceUntilIdle()

                // when
                tested.onBreweryClick(clickedId)

                // then
                eventObserver.awaitItem() `should be equal to` BreweriesListEvent.GoToDetails(
                    clickedId,
                )
                eventObserver.cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `given initialized and breweries displayed when favorite brewery clicked then brewery is removed from favorites`() =
        runTest {
            // given
            val ids = (1..PAGE_SIZE).map { "breweryId$it" }.toSet()
            val favoriteIds = ids.asSequence().shuffled().take(5).toSet()
            val clickedId = favoriteIds.random()
            val breweries =
                ids.map { createBrewery(id = it, isFavorite = favoriteIds.contains(it)) }
            val clickedBrewery = breweries.first { it.id == clickedId }
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
            } returns breweries
            tested
            advanceUntilIdle()

            // when
            tested.onFavoriteClick(clickedId, true)
            advanceUntilIdle()

            // then
            coVerify { repository.removeFromFavorites(clickedBrewery) }
        }

    @Test
    fun `given initialized and breweries displayed when not favorite brewery clicked then brewery is added to favorites`() =
        runTest {
            // given
            val ids = (1..PAGE_SIZE).map { "breweryId$it" }.toSet()
            val notFavoriteIds = ids.asSequence().shuffled().take(5).toSet()
            val clickedId = notFavoriteIds.random()
            val breweries =
                ids.map { createBrewery(id = it, isFavorite = !notFavoriteIds.contains(it)) }
            val clickedBrewery = breweries.first { it.id == clickedId }
            coEvery {
                repository.getBreweries(page = 1, pageSize = PAGE_SIZE, type = null)
            } returns breweries
            tested
            advanceUntilIdle()

            // when
            tested.onFavoriteClick(clickedId, false)
            advanceUntilIdle()

            // then
            coVerify { repository.addToFavorites(clickedBrewery) }
        }
}
