package com.deskbird.datasource.local.internal

import androidx.room.Database
import androidx.room.RoomDatabase
import com.deskbird.datasource.local.internal.dao.BreweriesDao
import com.deskbird.datasource.local.internal.model.BreweryEntity

@Database(entities = [BreweryEntity::class], version = 1)
internal abstract class BreweriesDatabase : RoomDatabase() {
    abstract fun breweriesDao(): BreweriesDao
}