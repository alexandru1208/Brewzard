package com.deskbird.brewzard.datasource.local.boundary

import com.deskbird.brewzard.datasource.local.internal.LocalBreweriesDataSourceImpl
import com.deskbird.brewzard.domain.data.LocalBreweriesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalDatasourceBoundaryModule {

    @Binds
    internal abstract fun bindLocalBreweriesDataSource(
        impl: LocalBreweriesDataSourceImpl,
    ): LocalBreweriesDataSource
}
