package com.deskbird.datasource.remote.internal.errors

import com.deskbird.domain.error.DataSourceException
import com.deskbird.domain.error.RemoteError
import io.mockk.every
import io.mockk.mockk
import okio.IOException
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import retrofit2.HttpException
import retrofit2.Response

private const val HTTP_CODE = 404
private const val HTTP_MESSAGE = "NOT FOUND"

internal class NetworkExceptionMapperTest {

    private val tested = NetworkExceptionMapper()

    @ParameterizedTest
    @MethodSource("paramsForTest")
    fun `should map exception correctly`(input: Throwable, expected: DataSourceException) {
        val actual = tested.map(input)
        actual.cause `should be equal to` input
        actual.error `should be equal to` expected.error
    }

    companion object {
        private val httpException = HttpException(mockk<Response<Any>> {
            every { code() } returns HTTP_CODE
            every { message() } returns HTTP_MESSAGE
        },)

        private val ioException = IOException()
        private val numberFormatException = NumberFormatException()
        private val unknownException = Exception()

        @JvmStatic
        fun paramsForTest() = listOf(
            Arguments.of(
                httpException,
                DataSourceException(
                    RemoteError.ServerError(
                        httpException,
                        HTTP_CODE,
                        HTTP_MESSAGE,
                    ),
                ),
            ),
            Arguments.of(
                ioException,
                DataSourceException(RemoteError.ConnectionError(ioException)),
            ),
            Arguments.of(
                numberFormatException,
                DataSourceException(RemoteError.MalformedDataError(numberFormatException)),
            ),
            Arguments.of(
                unknownException,
                DataSourceException(RemoteError.UnknownError(unknownException)),
            ),
        )
    }
}