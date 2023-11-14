package com.deskbird.domain.error

sealed interface RemoteError : DataSourceError {

    data class ConnectionError(override val cause: Throwable) : RemoteError
    data class ServerError(
        override val cause: Throwable,
        val errorCode: Int,
        val errorMessage: String = "",
    ) : RemoteError

    data class MalformedDataError(override val cause: Throwable) : RemoteError

    data class UnknownError(override val cause: Throwable) : RemoteError
}