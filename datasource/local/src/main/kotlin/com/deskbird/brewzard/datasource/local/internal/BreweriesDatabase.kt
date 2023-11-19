package com.deskbird.brewzard.datasource.local.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deskbird.brewzard.datasource.local.internal.dao.BreweryDao
import com.deskbird.brewzard.datasource.local.internal.model.BreweryEntity

@Database(entities = [BreweryEntity::class], version = 1)
internal abstract class BreweriesDatabase : RoomDatabase() {
    abstract fun breweriesDao(): BreweryDao
}
