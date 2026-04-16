package com.softgenix.abastock.features.communications.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class RegisterTokenRequestDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("token")  val token:  String
)