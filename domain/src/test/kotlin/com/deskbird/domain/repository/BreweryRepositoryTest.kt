package com.deskbird.domain.repository

import com.deskbird.domain.data.LocalBreweriesDataSource
import com.deskbird.domain.data.RemoteBreweriesDataSource
import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.model.BreweryType
import com.deskbird.domain.repository.TestBreweryFactory.createBrewery
import com.deskbird.test.util.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BreweryRepositoryTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val coroutineScope = TestScope(coroutineRule.dispatcher)
    private val localDataSource: LocalBreweriesDataSource = mockk(relaxed = true)
    private val remoteDataSource: RemoteBreweriesDataSource = mockk(relaxed = true)

    private val tested = BreweryRepository(
        coroutineScope,
        localDataSource,
        remoteDataSource,
    )

    @Test
    fun `given page, pageSize and type when get breweries called then try to take from remote datasource`() =
        runTest {
            // given
            val page = 1
            val pageSize = 10
            val type = BreweryType.LARGE

            // when
            tested.getBreweries(page, pageSize, type)

            // then
            coVerify { remoteDataSource.getBreweries(page, pageSize, type) }
        }

    @Test
    fun `given page, pageSize and type and remote returns breweries when get breweries called then check if breweries are favorite`() =
        runTest {
            // given
            val page = 1
            val pageSize = 10
            val type = BreweryType.LARGE
            val ids = (1..pageSize).map { "breweryId$it" }.toSet()
            val remoteBreweries = ids.map { createBrewery(id = it) }
            coEvery { remoteDataSource.getBreweries(page, pageSize, type) } returns remoteBreweries

            // when
            tested.getBreweries(page, pageSize, type)

            // then
            ids.forEach { coVerify { localDataSource.isFavorite(it) } }
        }

    @Test
    fun `given page, pageSize and type and remote returns breweries and some are favorite when get breweries called then update favorites`() =
        runTest {
            // given
            val page = 1
            val pageSize = 10
            val type = BreweryType.LARGE
            val ids = (1..pageSize).map { "breweryId$it" }.toSet()
            val favoriteIds = ids.asSequence().shuffled().take(5).toSet()
            val notFavoriteIds = ids - favoriteIds
            val remoteBreweries = ids.map { createBrewery(id = it) }
            coEvery { remoteDataSource.getBreweries(page, pageSize, type) } returns remoteBreweries
            favoriteIds.forEach { coEvery { localDataSource.isFavorite(it) } returns true }
            notFavoriteIds.forEach { coEvery { localDataSource.isFavorite(it) } returns false }

            // when
            tested.getBreweries(page, pageSize, type)
            advanceUntilIdle()

            // then
            coVerify {
                localDataSource.updateFavorites(match { it.map { it.id }.toSet() == favoriteIds })
            }
        }

    @Test
    fun `given page, pageSize and type and remote returns breweries and some are favorite when get breweries called then update breweries are returned`() =
        runTest {
            // given
            val page = 1
            val pageSize = 10
            val type = BreweryType.LARGE
            val ids = (1..pageSize).map { "breweryId$it" }.toSet()
            val favoriteIds = ids.asSequence().shuffled().take(5).toSet()
            val notFavoriteIds = ids - favoriteIds
            val remoteBreweries = ids.map { createBrewery(id = it) }
            val updatedBreweries =
                remoteBreweries.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
            coEvery { remoteDataSource.getBreweries(page, pageSize, type) } returns remoteBreweries
            favoriteIds.forEach { coEvery { localDataSource.isFavorite(it) } returns true }
            notFavoriteIds.forEach { coEvery { localDataSource.isFavorite(it) } returns false }

            // when
            val result = tested.getBreweries(page, pageSize, type)

            // then
            result `should be equal to` updatedBreweries
        }

    @Test
    fun `given page, pageSize and type and remote throws exception then propagate remote exception`() =
        runTest {
            // given
            val page = 1
            val pageSize = 10
            val type = BreweryType.LARGE
            val remoteException = mockk<DataSourceException>(relaxed = true)
            coEvery { remoteDataSource.getBreweries(page, pageSize, type) } throws remoteException

            // when
            val result = runCatching { tested.getBreweries(page, pageSize, type) }

            // then
            result.exceptionOrNull() `should be equal to` remoteException
        }

    @Test
    fun `given brewery id when get brewery called then try to take from remote datasource`() =
        runTest {
            // given
            val breweryId = "breweryId"

            // when
            tested.getBrewery(breweryId)

            // then
            coVerify { remoteDataSource.getBrewery(breweryId) }
        }

    @Test
    fun `given brewery id and remote datasource returns brewery when get brewery called then check if brewery is favorite`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val remoteBrewery = createBrewery(id = breweryId)
            coEvery { remoteDataSource.getBrewery(breweryId) } returns remoteBrewery

            // when
            tested.getBrewery(breweryId)

            // then
            coVerify { localDataSource.isFavorite(breweryId) }
        }

    @Test
    fun `given brewery id and remote datasource returns brewery and brewery is favorite when get brewery called then update favorite brewery`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val remoteBrewery = createBrewery(id = breweryId)
            val updatedBrewery = createBrewery(id = breweryId, isFavorite = true)
            coEvery { remoteDataSource.getBrewery(breweryId) } returns remoteBrewery
            coEvery { localDataSource.isFavorite(breweryId) } returns true

            // when
            tested.getBrewery(breweryId)
            advanceUntilIdle()

            // then
            coVerify { localDataSource.updateFavorites(listOf(updatedBrewery)) }
        }

    @Test
    fun `given brewery id and remote datasource throws exception then try to take brewery from local datasource`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val remoteException = mockk<DataSourceException>()
            coEvery { remoteDataSource.getBrewery(breweryId) } throws remoteException

            // when
            tested.getBrewery(breweryId)

            // then
            coVerify { localDataSource.getBrewery(breweryId) }
        }

    @Test
    fun `given brewery id and remote datasource throws exception and local datasource has brewery then return brewery from local datasource`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val localBrewery = createBrewery(id = breweryId)
            val remoteException = mockk<DataSourceException>()
            coEvery { remoteDataSource.getBrewery(breweryId) } throws remoteException
            coEvery { localDataSource.getBrewery(breweryId) } returns localBrewery

            // when
            val result = tested.getBrewery(breweryId)

            // then
            result `should be equal to` localBrewery
        }

    @Test
    fun `given brewery id and remote datasource throws exception and local datasource does not have brewery then propagate remote exception`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val remoteException = mockk<DataSourceException>()
            coEvery { remoteDataSource.getBrewery(breweryId) } throws remoteException
            coEvery { localDataSource.getBrewery(breweryId) } returns null

            // when
            val result = runCatching { tested.getBrewery(breweryId) }

            // then
            result.exceptionOrNull() `should be equal to` remoteException
        }

    @Test
    fun `when observe favorites called then observe favorites is called on the local datasource`() {
        // when
        tested.observeFavorites()

        // then
        verify { localDataSource.observeFavorites() }
    }

    @Test
    fun `given brewery when add to favorites called then add to favorites is called on the local datasource with same brewery`() =
        runTest {
            // given
            val brewery = createBrewery()

            // when
            tested.addToFavorites(brewery)

            // then
            coVerify { localDataSource.addToFavorites(brewery) }
        }

    @Test
    fun `given brewery when remove from favorites called then remove from favorites is called on the local datasource with same brewery`() =
        runTest {
            // given
            val brewery = createBrewery()

            // when
            tested.removeFromFavorites(brewery)

            // then
            coVerify { localDataSource.removeFromFavorites(brewery) }
        }
}