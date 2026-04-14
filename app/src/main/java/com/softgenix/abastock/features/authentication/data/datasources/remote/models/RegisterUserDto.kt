package com.softgenix.abastock.features.authentication.data.datasources.remote.models

import com.google.gson.annotations.SerializedName

data class RegisterUserDto(
    @SerializedName("id") val id: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("middleName") val middleName: String?,
    @SerializedName("firstSurname") val firstSurname: String,
    @SerializedName("secondLastName") val secondLastName: String,
    @SerializedName("phoneNumber") val phoneNumber: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)