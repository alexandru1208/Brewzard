package com.deskbird.brewzard.domain.error

class DataSourceException(val error: DataSourceError) : RuntimeException() {
    override val cause: Throwable = error.cause
    override val message: String? = error.cause.message
}
