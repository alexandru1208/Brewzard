package com.deskbird.datasource.remote.internal.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class BreweryApi(
    val id: String,
    val name: String,
    @Json(name = "brewery_type")
    val type: String,
    val city: String,
    @Json(name = "state_province")
    val stateProvince: String,
    @Json(name = "postal_code")
    val postalCode: String,
    val country: String,
    val latitude: String?,
    val longitude: String?,
    val phone: String?,
    @Json(name = "website_url")
    val websiteUrl: String?,
    val street: String?,
    val state: String
)