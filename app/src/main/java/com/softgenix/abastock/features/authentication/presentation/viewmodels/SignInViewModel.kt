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

            // 🔥 LOG 1: Vemos qué estamos a punto de mandar
            android.util.Log.d("AUTH_DEBUG", "🚀 Intentando Login -> Credencial: $credential, Pass: ${current.password}")

            loginUseCase(credentials)
                .onSuccess { tokens ->
                    // 🔥 LOG 2: Si jala, vemos que llegaron los tokens
                    android.util.Log.d("AUTH_DEBUG", "✅ ¡Login Exitoso! AccessToken: ${tokens.accessToken.take(10)}...")

                    tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            isAuthenticated = true
                        )
                    }
                }
                .onFailure { error ->
                    // 🔥 LOG 3: El chismoso principal. Aquí veremos si es 401, 404, o si tronó la red
                    android.util.Log.e("AUTH_DEBUG", "❌ Valió queso el login: ${error.message}", error)

                    _state.update {
                        it.copy(
                            isLoading = false,
                            // Mostramos el error real en la pantalla un ratito para depurar
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
