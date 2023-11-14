package com.deskbird.domain.error

class DataSourceException(error: DataSourceError) : RuntimeException() {
    override val cause: Throwable = error.cause
    override val message: String? = error.cause.message
}