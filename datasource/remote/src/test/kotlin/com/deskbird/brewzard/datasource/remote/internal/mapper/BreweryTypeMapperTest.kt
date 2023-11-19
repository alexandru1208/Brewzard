package com.deskbird.brewzard.datasource.remote.internal.mapper

import com.deskbird.brewzard.domain.error.DataSourceException
import com.deskbird.brewzard.domain.error.RemoteError
import com.deskbird.brewzard.domain.model.BreweryType
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.`should be instance of`
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class BreweryTypeMapperTest {

    private val tested = BreweryTypeMapper()

    @ParameterizedTest
    @MethodSource("paramsForMapToDomainTest")
    fun `should map brewery type to domain correctly`(input: String, expected: BreweryType) {
        tested.mapToDomain(input) `should be equal to` expected
    }

    @Test
    fun `should throw correct exception for unknown brewery type`() {
        val result = runCatching { tested.mapToDomain("unknown") }
        val exception = result.exceptionOrNull() as DataSourceException
        exception.error `should be instance of` RemoteError.MalformedDataError::class
        exception.cause `should be instance of` IllegalArgumentException::class
    }

    @ParameterizedTest
    @MethodSource("paramsForMapFromDomainTest")
    fun `should map brewery type from domain correctly`(input: BreweryType, expected: String) {
        tested.mapFromDomain(input) `should be equal to` expected
    }

    companion object {
        @JvmStatic
        fun paramsForMapToDomainTest() = listOf(
            Arguments.of("micro", BreweryType.MICRO),
            Arguments.of("nano", BreweryType.NANO),
            Arguments.of("regional", BreweryType.REGIONAL),
            Arguments.of("brewpub", BreweryType.BREWPUB),
            Arguments.of("large", BreweryType.LARGE),
            Arguments.of("planning", BreweryType.PLANNING),
            Arguments.of("bar", BreweryType.BAR),
            Arguments.of("contract", BreweryType.CONTRACT),
            Arguments.of("proprietor", BreweryType.PROPRIETOR),
            Arguments.of("closed", BreweryType.CLOSED),
        )

        @JvmStatic
        fun paramsForMapFromDomainTest() = listOf(
            Arguments.of(BreweryType.MICRO, "micro"),
            Arguments.of(BreweryType.NANO, "nano"),
            Arguments.of(BreweryType.REGIONAL, "regional"),
            Arguments.of(BreweryType.BREWPUB, "brewpub"),
            Arguments.of(BreweryType.LARGE, "large"),
            Arguments.of(BreweryType.PLANNING, "planning"),
            Arguments.of(BreweryType.BAR, "bar"),
            Arguments.of(BreweryType.CONTRACT, "contract", BreweryType.CONTRACT),
            Arguments.of(BreweryType.PROPRIETOR, "proprietor", BreweryType.PROPRIETOR),
            Arguments.of(BreweryType.CLOSED, "closed", BreweryType.CLOSED),
        )
    }
}
