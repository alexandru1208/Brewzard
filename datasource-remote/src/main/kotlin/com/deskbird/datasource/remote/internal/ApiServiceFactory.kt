package com.deskbird.datasource.remote.internal

import com.deskbird.datasource.remote.internal.errors.ErrorsCallAdapterFactory
import com.deskbird.datasource.remote.internal.errors.NetworkExceptionMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

internal class ApiServiceFactory @Inject constructor(
    private val exceptionMapper: NetworkExceptionMapper
) {

    fun create(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(ErrorsCallAdapterFactory(exceptionMapper))
            .client(okHttpClient)
            .build()

        return retrofit.create(ApiService::class.java)
    }

    companion object {
        private const val BASE_URL = "https://api.openbrewerydb.org/v1/"
    }
}