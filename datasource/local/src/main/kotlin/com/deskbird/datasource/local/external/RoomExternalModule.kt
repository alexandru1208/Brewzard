package com.deskbird.datasource.local.external

import com.deskbird.datasource.local.internal.LocalBreweriesDataSourceImpl
import com.deskbird.domain.data.LocalBreweriesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RoomExternalModule {

    @Binds
    internal abstract fun bindLocalBreweriesDataSource(
        impl: LocalBreweriesDataSourceImpl,
    ): LocalBreweriesDataSource
}