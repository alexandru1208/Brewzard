package com.deskbird.datasource.local.internal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.deskbird.datasource.local.internal.model.BreweryEntity
import com.deskbird.domain.model.Brewery

@Dao
internal interface BreweriesDao {
    @Query("SELECT * FROM breweries")
    suspend fun getAll(): List<BreweryEntity>

    @Query("SELECT EXISTS(SELECT * FROM breweries WHERE id = :breweryId)")
    fun isFavorite(breweryId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brewery: BreweryEntity)

    @Delete
    suspend fun delete(brewery: BreweryEntity)


}