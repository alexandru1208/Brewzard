package com.deskbird.domain.error

sealed interface DataSourceError {
    val cause: Throwable
}
