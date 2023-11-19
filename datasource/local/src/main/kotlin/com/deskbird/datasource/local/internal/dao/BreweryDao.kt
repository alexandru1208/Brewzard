package com.deskbird.datasource.local.internal.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.deskbird.datasource.local.internal.model.BreweryEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface BreweryDao {
    @Query("SELECT * FROM breweries")
    fun observeAll(): Flow<List<BreweryEntity>>

    @Query("SELECT * FROM breweries WHERE id==:breweryId")
    suspend fun getBrewery(breweryId: String): BreweryEntity?

    @Query("SELECT EXISTS(SELECT * FROM breweries WHERE id = :breweryId)")
    suspend fun isFavorite(breweryId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brewery: BreweryEntity)

    @Update
    suspend fun update(breweries: List<BreweryEntity>)

    @Delete
    suspend fun delete(brewery: BreweryEntity)
}