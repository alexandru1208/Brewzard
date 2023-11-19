package com.deskbird.brewzard.domain.error

sealed interface DataSourceError {
    val cause: Throwable
}
