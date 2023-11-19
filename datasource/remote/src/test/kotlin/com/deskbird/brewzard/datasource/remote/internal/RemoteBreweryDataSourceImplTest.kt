package com.deskbird.brewzard.datasource.remote.internal

import com.deskbird.brewzard.datasource.remote.internal.mapper.BreweryMapper
import com.deskbird.brewzard.datasource.remote.internal.mapper.BreweryTypeMapper
import com.deskbird.brewzard.datasource.remote.internal.model.BreweryApi
import com.deskbird.brewzard.domain.model.Brewery
import com.deskbird.brewzard.domain.model.BreweryType
import com.deskbird.brewzard.domain.util.DispatchersProvider.Companion.testDispatchersProvider
import com.deskbird.brewzard.test.util.CoroutinesTestExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(CoroutinesTestExtension::class)
internal class RemoteBreweryDataSourceImplTest {

    private val dispatchersProvider = testDispatchersProvider(CoroutinesTestExtension.dispatcher)
    private val apiService = mockk<ApiService>()
    private val breweryTypeMapper = mockk<BreweryTypeMapper>()
    private val breweryMapper = mockk<BreweryMapper>()

    private val tested = RemoteBreweryDataSourceImpl(
        dispatchersProvider,
        apiService,
        breweryTypeMapper,
        breweryMapper,
    )

    @Test
    fun `given api services returns api brewery and mapper maps it to domain brewery when brewery for id requested then return domain brewery`() =
        runTest {
            // given
            val breweryId = "breweryId"
            val breweryApi = mockk<BreweryApi>()
            val breweryDomain = mockk<Brewery>()
            coEvery { apiService.getBrewery(breweryId) } returns breweryApi
            coEvery { breweryMapper.mapToDomain(breweryApi) } returns breweryDomain

            // when
            val result = tested.getBrewery(breweryId)

            // then
            result `should be equal to` breweryDomain
        }

    @Test
    fun `given api services returns api breweries and mapper maps them to domain breweries when breweries for ids requested then return domain breweries`() =
        runTest {
            // given
            val breweryIds = (1..10).map { "brewerId$it" }
            val joinedIds = breweryIds.joinToString(",")
            val breweriesApi = (1..10).map { mockk<BreweryApi>() }
            val breweriesDomain = (1..10).map { mockk<Brewery>() }
            coEvery { apiService.getBreweries(joinedIds) } returns breweriesApi
            coEvery { breweryMapper.mapToDomain(breweriesApi) } returns breweriesDomain

            // when
            val result = tested.getBreweries(breweryIds)

            // then
            result `should be equal to` breweriesDomain
        }

    @Test
    fun `given api services returns api breweries and mapper maps them to domain breweries when breweries for page requested then return domain breweries`() =
        runTest {
            // given
            val page = 1
            val pageSize = 10
            val typeDomain = BreweryType.BREWPUB
            val typeApi = "brewpub"
            val breweriesApi = (1..pageSize).map { mockk<BreweryApi>() }
            val breweriesDomain = (1..pageSize).map { mockk<Brewery>() }
            coEvery { breweryTypeMapper.mapFromDomain(typeDomain) } returns typeApi
            coEvery { apiService.getBreweries(page, pageSize, typeApi) } returns breweriesApi
            coEvery { breweryMapper.mapToDomain(breweriesApi) } returns breweriesDomain

            // when
            val result = tested.getBreweries(page, pageSize, typeDomain)

            // then
            result `should be equal to` breweriesDomain
        }
}
