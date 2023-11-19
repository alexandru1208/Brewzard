package com.deskbird.brewzard.datasource.local.internal.mapper

import com.deskbird.brewzard.domain.model.BreweryType
import javax.inject.Inject

internal class BreweryTypeMapper @Inject constructor() {

    fun mapToDomain(type: String): BreweryType = when (type) {
        "micro" -> BreweryType.MICRO
        "nano" -> BreweryType.NANO
        "regional" -> BreweryType.REGIONAL
        "brewpub" -> BreweryType.BREWPUB
        "large" -> BreweryType.LARGE
        "planning" -> BreweryType.PLANNING
        "bar" -> BreweryType.BAR
        "contract" -> BreweryType.CONTRACT
        "proprietor" -> BreweryType.PROPRIETOR
        "closed" -> BreweryType.CLOSED
        else -> throw IllegalArgumentException("Unknown brewery type received")
    }

    fun mapFromDomain(type: BreweryType): String = when (type) {
        BreweryType.MICRO -> "micro"
        BreweryType.NANO -> "nano"
        BreweryType.REGIONAL -> "regional"
        BreweryType.BREWPUB -> "brewpub"
        BreweryType.LARGE -> "large"
        BreweryType.PLANNING -> "planning"
        BreweryType.BAR -> "bar"
        BreweryType.CONTRACT -> "contract"
        BreweryType.PROPRIETOR -> "proprietor"
        BreweryType.CLOSED -> "closed"
    }
}
