package com.softgenix.abastock.features.authentication.data.datasources.remote.repositories

import com.softgenix.abastock.features.authentication.data.datasources.remote.api.AuthApi
import com.softgenix.abastock.features.authentication.data.datasources.remote.mapper.toDomain
import com.softgenix.abastock.features.authentication.data.datasources.remote.mapper.toDto
import com.softgenix.abastock.features.authentication.domain.entities.AuthTokens
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials
import com.softgenix.abastock.features.authentication.domain.entities.RegisterUser
import com.softgenix.abastock.features.authentication.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {
    override suspend fun registerUser(user: RegisterUser): Result<Unit> {
        return api.registerUser(user)
    }

    override suspend fun login(credentials: LoginCredentials): AuthTokens {
        val response = api.login(credentials.toDto())

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.toDomain()
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Error desconocido del servidor"
            throw Exception("HTTP ${response.code()}: $errorMsg")
        }
    }
}