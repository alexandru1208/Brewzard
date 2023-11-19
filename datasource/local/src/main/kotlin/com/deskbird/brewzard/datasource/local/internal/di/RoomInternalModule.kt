package com.deskbird.brewzard.datasource.local.internal.di

import com.deskbird.brewzard.datasource.local.internal.BreweriesDatabase
import com.deskbird.brewzard.datasource.local.internal.DatabaseFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RoomInternalModule {

    companion object {
        @Provides
        fun provideDatabase(factory: DatabaseFactory): BreweriesDatabase = factory.create()

        @Provides
        fun provideBreweriesDao(db: BreweriesDatabase) = db.breweriesDao()
    }
}
