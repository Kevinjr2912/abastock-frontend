package com.softgenix.abastock.features.inventory.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class CreateInventoryDto(
    @SerializedName("inventoryId") val inventoryId: String,
    @SerializedName("storeId") val storeId: String,
    @SerializedName("presentationId") val presentationId: String
)