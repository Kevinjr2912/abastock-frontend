package com.softgenix.abastock.features.authentication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials
import com.softgenix.abastock.features.authentication.domain.entities.RegisterUser
import com.softgenix.abastock.features.authentication.domain.usecases.LoginUseCase
import com.softgenix.abastock.features.authentication.domain.usecases.RegisterUserUseCase
import com.softgenix.abastock.features.authentication.presentation.screens.SignUpUIState
import com.softgenix.abastock.features.communications.domain.usecases.RegisterDeviceTokenUseCase
import com.softgenix.abastock.features.store.domain.entities.Store
import com.softgenix.abastock.features.store.domain.usecases.CreateStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val createStoreUseCase: CreateStoreUseCase,
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpUIState())
    val state = _state.asStateFlow()

    fun onStoreNameChange(value: String) = _state.update { it.copy(storeName = value) }
    fun onNameChange(value: String) = _state.update { it.copy(name = value) }
    fun onLastNameChange(value: String) = _state.update { it.copy(lastName = value) }
    fun onPhoneNumber(value: String) = _state.update { it.copy(phoneNumber = value) }
    fun onEmailChange(value: String) = _state.update { it.copy(email = value) }
    fun onPasswordChange(value: String) = _state.update { it.copy(password = value) }
    fun onConfirmPasswordChange(value: String) = _state.update { it.copy(confirmPassword = value) }
    fun onTogglePasswordVisible() = _state.update { it.copy(passwordVisible = !it.passwordVisible) }
    fun onToggleConfirmPasswordVisible() = _state.update { it.copy(confirmPasswordVisible = !it.confirmPasswordVisible) }

    fun onSignUp() {
        val current = _state.value
        if (!current.isFormValid) return

        val userId = UUID.randomUUID().toString()
        val nameParts = splitNames(current.name)
        val surnameParts = splitSurnames(current.lastName)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val user = RegisterUser(
                id = userId,
                firstName = nameParts.first,
                middleName = nameParts.second,
                firstSurname = surnameParts.first,
                secondLastName = surnameParts.second,
                phoneNumber = current.phoneNumber,
                email = current.email,
                password = current.password
            )

            registerUserUseCase(user)
                .onSuccess {
                    val store = Store(
                        id = UUID.randomUUID().toString(),
                        name = current.storeName,
                        userId = userId
                    )
                    createStoreUseCase(store)
                        .onSuccess {
                            // Con esto indicamos un registro completo para luego ir a SuccessScreen
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    email = current.email,
                                    password = current.password
                                )
                            }
                        }
                        .onFailure { e ->
                            _state.update { it.copy(isLoading = false, error = e.message) }
                        }
                }
                .onFailure { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
        }
    }
    private fun splitNames(fullName: String): Pair<String, String> {
        val parts = fullName.trim().split("\\s+".toRegex())
        return Pair(
            parts.firstOrNull() ?: "",
            parts.drop(1).joinToString(" ")
        )
    }

    private fun splitSurnames(fullSurname: String): Pair<String, String> {
        val parts = fullSurname.trim().split("\\s+".toRegex())
        return Pair(
            parts.firstOrNull() ?: "",
            parts.drop(1).joinToString(" ")
        )
    }

    fun onAutoLogin() {
        val current = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val credentials = if (current.email.isNotBlank())
                LoginCredentials(email = current.email, password = current.password)
            else
                LoginCredentials(phoneNumber = current.phoneNumber, password = current.password)

            loginUseCase(credentials)
                .onSuccess { tokens ->
                    tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                    registerFcmToken()   // ← NUEVO
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                .onFailure { e ->
                    val msg = when {
                        e.message?.contains("409") == true -> "El correo o teléfono ya está registrado"
                        e.message?.contains("network") == true -> "Sin conexión"
                        else -> "Error al crear la cuenta"
                    }
                    _state.update { it.copy(isLoading = false, error = msg) }
                }
        }
    }

    private fun registerFcmToken() {
        viewModelScope.launch {
            try {
                val fcmToken = com.google.firebase.messaging.FirebaseMessaging
                    .getInstance().token.await()
                registerDeviceTokenUseCase(fcmToken)
                    .onFailure { android.util.Log.w("FCM", "Token no registrado: ${it.message}") }
            } catch (e: Exception) {
                android.util.Log.e("FCM", "Error FCM: ${e.message}")
            }
        }
    }

    fun onSuccessScreenEntered() {
        _state.update { it.copy(isLoading = true) }
    }

}
