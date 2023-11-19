package com.deskbird.datasource.local.internal.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "breweries")
internal data class BreweryEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "brewery_type")
    val type: String,
    val city: String,
    @ColumnInfo(name = "state_province")
    val stateProvince: String,
    @ColumnInfo(name = "postal_code")
    val postalCode: String,
    val country: String,
    val latitude: Float?,
    val longitude: Float?,
    val phone: String?,
    @ColumnInfo(name = "website_url")
    val websiteUrl: String?,
    val street: String?,
    val state: String,
)
