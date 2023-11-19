package com.deskbird.brewzard.datasource.remote.boundary

import com.deskbird.brewzard.datasource.remote.internal.RemoteBreweryDataSourceImpl
import com.deskbird.brewzard.domain.data.RemoteBreweryDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDatasourceBoundaryModule {

    @Binds
    internal abstract fun bindRemoteBreweriesDatasource(
        impl: RemoteBreweryDataSourceImpl,
    ): RemoteBreweryDataSource
}
