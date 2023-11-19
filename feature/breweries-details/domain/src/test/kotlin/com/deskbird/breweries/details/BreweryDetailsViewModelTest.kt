package com.deskbird.breweries.details

import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.repository.BreweryRepository
import com.deskbird.test.data.TestBreweryFactory.createBrewery
import com.deskbird.test.util.CoroutinesTestExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

private const val BREWERY_ID = "breweryId"

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
class BreweryDetailsViewModelTest {

    private val repository = mockk<BreweryRepository>(relaxed = true)

    private val tested by lazy {
        BreweryDetailsViewModel(BREWERY_ID, repository)
    }

    @Test
    fun `when initialized brewery is fetched for id`() = runTest {
        // when
        tested
        advanceUntilIdle()

        // then
        coVerify { repository.getBrewery(BREWERY_ID) }
    }

    @Test
    fun `when initialized ui has correct state`() {
        // when
        tested

        // then
        tested.state.value `should be equal to` BreweryDetailsScreenState(
            brewery = null,
            progressIndicatorVisible = true,
            errorVisible = false,
        )
    }

    @Test
    fun `given brewery fetched when initialized ui has correct state`() = runTest {
        // given
        val brewery = createBrewery(BREWERY_ID)
        coEvery { repository.getBrewery(BREWERY_ID) } returns brewery

        // when
        tested
        advanceUntilIdle()

        // then
        tested.state.value `should be equal to` BreweryDetailsScreenState(
            brewery = brewery,
            progressIndicatorVisible = false,
            errorVisible = false,
        )
    }

    @Test
    fun `given brewery fetch failed when initialized ui has correct state`() = runTest {
        // given
        val exception = mockk<DataSourceException>()
        coEvery { repository.getBrewery(BREWERY_ID) } throws exception

        // when
        tested
        advanceUntilIdle()

        // then
        tested.state.value `should be equal to` BreweryDetailsScreenState(
            brewery = null,
            progressIndicatorVisible = false,
            errorVisible = true,
        )
    }

    @Test
    fun `given initialized and brewery fetch failed when try again clicked fetch is called again`() =
        runTest {
            // given
            val exception = mockk<DataSourceException>()
            coEvery { repository.getBrewery(BREWERY_ID) } throws exception
            tested
            advanceUntilIdle()

            // when
            tested.onTryAgainClick()
            advanceUntilIdle()

            // then
            coVerify(exactly = 2) { repository.getBrewery(BREWERY_ID) }
        }

    @Test
    fun `given initialized and fetched brewery is favorite when favorite button clicked then brewery is removed from favorites`() =
        runTest {
            // given
            val brewery = createBrewery(BREWERY_ID, isFavorite = true)
            coEvery { repository.getBrewery(BREWERY_ID) } returns brewery
            tested
            advanceUntilIdle()

            // when
            tested.onFavoriteButtonClick(isFavorite = true)
            advanceUntilIdle()

            // then
            coVerify { repository.removeFromFavorites(brewery) }
        }

    @Test
    fun `given initialized and fetched brewery is not favorite when favorite button clicked then brewery is added to favorites`() =
        runTest {
            // given
            val brewery = createBrewery(BREWERY_ID, isFavorite = false)
            coEvery { repository.getBrewery(BREWERY_ID) } returns brewery
            tested
            advanceUntilIdle()

            // when
            tested.onFavoriteButtonClick(isFavorite = false)
            advanceUntilIdle()

            // then
            coVerify { repository.addToFavorites(brewery) }
        }

    @Test
    fun `given initialized and fetched brewery is favorite when favorite button clicked then ui has correct state`() =
        runTest {
            // given
            val brewery = createBrewery(BREWERY_ID, isFavorite = true)
            val updatedBrewery = brewery.copy(isFavorite = false)
            coEvery { repository.getBrewery(BREWERY_ID) } returns brewery
            tested
            advanceUntilIdle()

            // when
            tested.onFavoriteButtonClick(isFavorite = true)
            advanceUntilIdle()

            // then
            // then
            tested.state.value `should be equal to` BreweryDetailsScreenState(
                brewery = updatedBrewery,
                progressIndicatorVisible = false,
                errorVisible = false,
            )
        }
}