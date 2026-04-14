package com.softgenix.abastock.features.authentication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials
import com.softgenix.abastock.features.authentication.domain.usecases.LoginUseCase
import com.softgenix.abastock.features.authentication.presentation.screens.SignInUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(SignInUIState())
    val state = _state.asStateFlow()

    fun onCredentialChange(value: String) = _state.update { it.copy(credential = value) }
    fun onPasswordChange(value: String) = _state.update { it.copy(password = value) }
    fun onTogglePasswordVisible() = _state.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun onSignIn() {
        val current = _state.value
        val credential = current.credential.trim()

        val isEmail = credential.contains("@")

        if (isEmail) {
            if (!isValidGmail(credential)) {
                _state.update { it.copy(error = "Solo se permiten correos Gmail") }
                return
            }
        } else {
            if (!isValidPhone(credential)) {
                _state.update { it.copy(error = "El número debe tener 10 dígitos") }
                return
            }
        }

        val credentials = if (isEmail) {
            LoginCredentials(email = credential, password = current.password)
        } else {
            LoginCredentials(phoneNumber = credential, password = current.password)
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            loginUseCase(credentials)
                .onSuccess { tokens ->

                    tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true
                        )
                    }
                }
                .onFailure { error ->

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = "Error del Back: ${error.message ?: "Desconocido"}"
                        )
                    }
                }
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^\\d{10}$"))
    }

    private fun isValidGmail(email: String): Boolean {
        return email.matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))
    }
}
