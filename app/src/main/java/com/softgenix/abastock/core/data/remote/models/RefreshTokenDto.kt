package com.softgenix.abastock.core.data.remote.models

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestDto(
    @SerializedName("refresh_token") val refreshToken: String
)

data class RefreshTokenResponseDto(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String
)