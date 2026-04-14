package com.softgenix.abastock.features.authentication.data.datasources.remote.mapper

import com.softgenix.abastock.features.authentication.data.datasources.remote.models.AuthTokensDto
import com.softgenix.abastock.features.authentication.data.datasources.remote.models.LoginCredentialsDto
import com.softgenix.abastock.features.authentication.domain.entities.AuthTokens
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials

fun LoginCredentials.toDto(): LoginCredentialsDto {
    return LoginCredentialsDto(
        email = this.email,
        phoneNumber = this.phoneNumber,
        password = this.password
    )
}

fun AuthTokensDto.toDomain(): AuthTokens {
    return AuthTokens(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}