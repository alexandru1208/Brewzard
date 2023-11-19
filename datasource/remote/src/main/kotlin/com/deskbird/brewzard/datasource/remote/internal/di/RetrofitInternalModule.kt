package com.deskbird.brewzard.datasource.remote.internal.di

import com.deskbird.brewzard.datasource.remote.internal.ApiServiceFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RetrofitInternalModule {

    companion object {
        @Provides
        fun provideApiService(factory: ApiServiceFactory) = factory.create()
    }
}
