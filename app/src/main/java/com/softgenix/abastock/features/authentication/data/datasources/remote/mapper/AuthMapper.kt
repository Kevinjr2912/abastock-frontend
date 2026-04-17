package com.softgenix.abastock.features.authentication.data.datasources.remote.mapper

import com.softgenix.abastock.features.authentication.data.datasources.remote.models.AuthTokensDto
import com.softgenix.abastock.features.authentication.data.datasources.remote.models.LoginCredentialsDto
import com.softgenix.abastock.features.authentication.data.datasources.remote.models.RegisterUserDto
import com.softgenix.abastock.features.authentication.domain.entities.AuthTokens
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials
import com.softgenix.abastock.features.authentication.domain.entities.RegisterUser

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

fun RegisterUser.toDto(): RegisterUserDto {
    return RegisterUserDto(
        id = this.id,
        firstName = this.firstName,
        middleName = this.middleName.takeIf { it.isNotBlank() },
        firstSurname = this.firstSurname,
        secondLastName = this.secondLastName,
        phoneNumber = this.phoneNumber,
        email = this.email,
        password = this.password
    )
}