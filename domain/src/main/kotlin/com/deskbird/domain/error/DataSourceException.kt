package com.deskbird.domain.error

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch

class DataSourceException(error: DataSourceError) : RuntimeException() {
    override val cause: Throwable = error.cause
    override val message: String? = error.cause.message
}

fun <T> Flow<T>.catchDataSourceException(
    action: suspend FlowCollector<T>.(exception: DataSourceException) -> Unit,
) = catch {
    if (it is DataSourceException) {
        action(it)
    } else {
        throw it
    }
}