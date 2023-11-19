package com.deskbird.brewzard.di

import com.deskbird.brewzard.domain.util.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppSingletonModule {

    @Provides
    fun provideDispatchersProvider(): DispatchersProvider = object : DispatchersProvider {
        override val io: CoroutineDispatcher = Dispatchers.IO
        override val default: CoroutineDispatcher = Dispatchers.Default
        override val main: CoroutineDispatcher = Dispatchers.Main
        override val immediate: CoroutineDispatcher = Dispatchers.Main.immediate
    }
}
