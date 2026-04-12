package com.softgenix.abastock.features.purchases.data.datasources.remote.models

data class AbastockResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T
)