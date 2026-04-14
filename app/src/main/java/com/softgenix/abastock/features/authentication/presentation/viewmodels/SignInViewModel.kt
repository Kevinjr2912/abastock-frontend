package com.softgenix.abastock.features.authentication.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.authentication.domain.entities.LoginCredentials
import com.softgenix.abastock.features.authentication.domain.usecases.LoginUseCase
import com.softgenix.abastock.features.authentication.presentation.screens.SignInUIState
import com.softgenix.abastock.features.communications.domain.usecases.RegisterDeviceTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SignInUIState())
    val state = _state.asStateFlow()

    fun onCredentialChange(value: String) = _state.update { it.copy(credential = value) }
    fun onPasswordChange(value: String)   = _state.update { it.copy(password = value) }
    fun onTogglePasswordVisible()         = _state.update { it.copy(passwordVisible = !it.passwordVisible) }

    fun onSignIn() {
        val current    = _state.value
        val credential = current.credential.trim()
        val isEmail    = credential.contains("@")

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

        val credentials = if (isEmail)
            LoginCredentials(email = credential, password = current.password)
        else
            LoginCredentials(phoneNumber = credential, password = current.password)

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            loginUseCase(credentials)
                .onSuccess { tokens ->
                    tokenManager.saveTokens(tokens.accessToken, tokens.refreshToken)
                    registerFcmToken()   // ← NUEVO: registrar token tras login
                    _state.update { it.copy(isLoading = false, isAuthenticated = true) }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(isLoading = false,
                            error = "Error: ${error.message ?: "Desconocido"}")
                    }
                }
        }
    }

    // ─── FCM ─────────────────────────────────────────────────────────────────
    private fun registerFcmToken() {
        viewModelScope.launch {
            android.util.Log.d("FCM_DEBUG", "1️⃣ registerFcmToken() iniciado")

            val session = tokenManager.getSession()
            android.util.Log.d("FCM_DEBUG", "2️⃣ Session: userId=${session?.userId}, storeId=${session?.storeId}")

            if (session == null) {
                android.util.Log.e("FCM_DEBUG", "❌ Sin sesión — no se puede registrar el token")
                return@launch
            }

            try {
                android.util.Log.d("FCM_DEBUG", "3️⃣ Solicitando token a Firebase...")
                val fcmToken = com.google.firebase.messaging.FirebaseMessaging
                    .getInstance().token.await()
                android.util.Log.d("FCM_DEBUG", "4️⃣ Token FCM obtenido: $fcmToken")

                registerDeviceTokenUseCase(fcmToken)
                    .onSuccess {
                        android.util.Log.d("FCM_DEBUG", "✅ Token registrado en backend exitosamente")
                    }
                    .onFailure {
                        android.util.Log.e("FCM_DEBUG", "❌ Falló registro en backend: ${it.message}", it)
                    }

            } catch (e: Exception) {
                android.util.Log.e("FCM_DEBUG", "❌ Excepción obteniendo token Firebase: ${e.message}", e)
            }
        }
    }

    private fun isValidPhone(phone: String) = phone.matches(Regex("^\\d{10}$"))
    private fun isValidGmail(email: String) =
        email.matches(Regex("^[A-Za-z0-9+_.-]+@gmail\\.com$"))
}