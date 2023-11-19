package com.deskbird.datasource.remote.boundary

import com.deskbird.datasource.remote.internal.RemoteBreweryDataSourceImpl
import com.deskbird.domain.data.RemoteBreweryDataSource
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
