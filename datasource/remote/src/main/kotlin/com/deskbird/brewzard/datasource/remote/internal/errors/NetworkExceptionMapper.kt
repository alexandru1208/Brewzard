package com.deskbird.brewzard.datasource.remote.internal.errors

import com.deskbird.brewzard.domain.error.DataSourceException
import com.deskbird.brewzard.domain.error.RemoteError
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

internal class NetworkExceptionMapper @Inject constructor() {

    fun map(exception: Throwable) = when (exception) {
        is HttpException -> DataSourceException(
            RemoteError.ServerError(
                exception,
                exception.code(),
                exception.message(),
            ),
        )

        is IOException -> DataSourceException(RemoteError.ConnectionError(exception))
        is NumberFormatException -> DataSourceException(RemoteError.MalformedDataError(exception))
        else -> DataSourceException(RemoteError.UnknownError(exception))
    }
}
