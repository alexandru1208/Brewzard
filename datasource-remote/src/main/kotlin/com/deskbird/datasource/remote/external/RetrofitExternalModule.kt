package com.deskbird.datasource.remote.external

import com.deskbird.datasource.remote.internal.RemoteBreweriesDataSourceImpl
import com.deskbird.domain.data.RemoteBreweriesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RetrofitExternalModule {

    @Binds
    internal abstract fun bindRemoteBreweriesDatasource(
        impl: RemoteBreweriesDataSourceImpl
    ): RemoteBreweriesDataSource
}