package com.softgenix.abastock.features.authentication.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class AuthTokensDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)

// data class TokenBodyDto(
   // val accessToken: String,
    // val refreshToken: String
// )