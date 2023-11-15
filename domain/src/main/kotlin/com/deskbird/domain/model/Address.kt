package com.deskbird.domain.model

data class Address(
    val street: String?,
    val city: String,
    val stateProvince: String,
    val state: String,
    val country: String,
    val postalCode: String,
)

