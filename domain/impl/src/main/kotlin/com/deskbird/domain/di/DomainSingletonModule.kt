package com.deskbird.domain.di

import com.deskbird.domain.repository.BreweryRepository
import com.deskbird.domain.repository.BreweryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainSingletonModule {

    @Binds
    internal abstract fun bindBreweryRepository(impl: BreweryRepositoryImpl): BreweryRepository

    companion object {
        @Provides
        @Singleton
        @ApplicationScope
        fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())
    }
}