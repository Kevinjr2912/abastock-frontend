package com.softgenix.abastock.features.authentication.data.datasources.remote.api

import com.softgenix.abastock.features.authentication.data.datasources.remote.models.AuthTokensDto
import com.softgenix.abastock.features.authentication.data.datasources.remote.models.LoginCredentialsDto
import com.softgenix.abastock.features.authentication.data.datasources.remote.models.RegisterUserDto
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials
import com.softgenix.abastock.features.authentication.domain.entities.RegisterUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

        @POST("auth/sign-up")
        suspend fun registerUser(
            @Body user: RegisterUserDto
        ): Response<Unit>


        @POST("auth/sign-in")
        suspend fun login(@Body credentials: LoginCredentialsDto): Response<AuthTokensDto>
    }