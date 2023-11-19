package com.deskbird.brewzard.di

import androidx.lifecycle.SavedStateHandle
import com.deskbird.brewzard.Screen
import com.deskbird.brewzard.domain.di.BreweryId
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppViewModelModule {

    companion object {
        @Provides
        @BreweryId
        fun provideBreweryId(savedStateHandle: SavedStateHandle): String {
            return checkNotNull(savedStateHandle[Screen.Details.ARGUMENT_KEY])
        }
    }
}
