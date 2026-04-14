package com.softgenix.abastock.features.store.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class StoreDto(
    @SerializedName("storeId") val storeId: String,
    @SerializedName("name") val name: String,
    @SerializedName("userId") val userId: String
)