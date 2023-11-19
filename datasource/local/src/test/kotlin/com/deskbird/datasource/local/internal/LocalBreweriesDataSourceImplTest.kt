package com.deskbird.datasource.local.internal

import app.cash.turbine.turbineScope
import com.deskbird.datasource.local.internal.dao.BreweryDao
import com.deskbird.datasource.local.internal.mapper.BreweryMapper
import com.deskbird.datasource.local.internal.model.BreweryEntity
import com.deskbird.domain.model.Brewery
import com.deskbird.domain.util.DispatchersProvider.Companion.testDispatchersProvider
import com.deskbird.test.util.CoroutinesTestExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
class LocalBreweriesDataSourceImplTest {
    private val dispatchersProvider = testDispatchersProvider(CoroutinesTestExtension.dispatcher)

    private val breweryDao = mockk<BreweryDao>(relaxed = true)
    private val breweryMapper = mockk<BreweryMapper>()

    private val tested = LocalBreweriesDataSourceImpl(
        dispatchersProvider,
        breweryDao,
        breweryMapper,
    )

    @Test
    fun `given dao returns is favorite value when is favorite requested then correct value is returned`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val isFavorite = true
            coEvery { breweryDao.isFavorite(breweryId) } returns isFavorite

            // when
            val result = tested.isFavorite(breweryId)

            // then
            result `should be equal to` isFavorite
        }

    @Test
    fun `given dao returns brewery entity and mapper maps it to domain brewery when brewery for id requested then return domain brewery`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val breweryEntity = mockk<BreweryEntity>()
            val breweryDomain = mockk<Brewery>()
            coEvery { breweryDao.getBrewery(breweryId) } returns breweryEntity
            coEvery { breweryMapper.mapToDomain(breweryEntity) } returns breweryDomain

            // when
            val result = tested.getBrewery(breweryId)

            // then
            result `should be equal to` breweryDomain
        }

    @Test
    fun `given mapper maps domain breweries to brewery entities when update breweries called then update breweries is called on dao with mapped breweries`() =
        runTest {
            // given
            val breweriesDomain = (1..10).map { mockk<Brewery>() }
            val breweryEntities = (1..10).map { mockk<BreweryEntity>() }
            coEvery { breweryMapper.mapFromDomain(breweriesDomain) } returns breweryEntities

            // when
            tested.updateFavorites(breweriesDomain)

            // then
            coVerify { breweryDao.update(breweryEntities) }
        }

    @Test
    fun `given mapper maps domain brewery to brewery entity when add to favorite called then insert is called on dao with mapped brewery`() =
        runTest {
            // given
            val breweryDomain = mockk<Brewery>()
            val breweryEntity = mockk<BreweryEntity>()
            coEvery { breweryMapper.mapFromDomain(breweryDomain) } returns breweryEntity

            // when
            tested.addToFavorites(breweryDomain)

            // then
            coVerify { breweryDao.insert(breweryEntity) }
        }

    @Test
    fun `given mapper maps domain brewery to brewery entity when remove from favorite called then delete is called on dao with mapped brewery`() =
        runTest {
            // given
            val breweryDomain = mockk<Brewery>()
            val breweryEntity = mockk<BreweryEntity>()
            coEvery { breweryMapper.mapFromDomain(breweryDomain) } returns breweryEntity

            // when
            tested.removeFromFavorites(breweryDomain)

            // then
            coVerify { breweryDao.delete(breweryEntity) }
        }

    @Test
    fun `given mapper maps brewery entities to domain breweries and dao emits all entities when flow observed then mapped domain breweries are emitted`() =
        runTest {
            turbineScope {
                // given
                val breweriesDomain = (1..10).map { mockk<Brewery>() }
                val breweryEntities = (1..10).map { mockk<BreweryEntity>() }
                every { breweryDao.observeAll() } returns flowOf(breweryEntities)
                coEvery { breweryMapper.mapToDomain(breweryEntities) } returns breweriesDomain

                // when
                val flowObserver = tested.observeFavorites().testIn(this)

                // then
                flowObserver.awaitItem() `should be equal to` breweriesDomain
                flowObserver.cancelAndIgnoreRemainingEvents()
            }
        }
}
