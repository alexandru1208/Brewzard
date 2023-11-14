package com.deskbird.domain.di

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

    companion object {
        @Provides
        @Singleton
        @ApplicationScope
        fun providesApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())
    }
}