package com.deskbird.datasource.local.internal

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class DatabaseFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun create() = Room.databaseBuilder(
        context = context,
        klass = BreweriesDatabase::class.java,
        name = "favorite_breweries",
    ).build()
}