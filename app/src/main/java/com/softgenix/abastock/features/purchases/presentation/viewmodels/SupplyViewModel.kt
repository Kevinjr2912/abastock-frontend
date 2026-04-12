package com.softgenix.abastock.features.purchases.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.hardware.domain.VibrationManager
import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransaction
import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransactionItem
import com.softgenix.abastock.features.purchases.domain.usecases.ConfirmPurchaseUseCase
import com.softgenix.abastock.features.purchases.presentation.screens.SupplyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SupplyViewModel @Inject constructor(
    private val confirmPurchaseUseCase: ConfirmPurchaseUseCase,
    private val vibrationManager: VibrationManager,
    savedStateHandle: SavedStateHandle,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SupplyUiState())
    val uiState = _uiState.asStateFlow()

    val storeId: String = tokenManager.getSession()?.storeId
        ?: savedStateHandle.get<String>("storeId")
        ?: ""

    var currentStoreId: String = ""

    fun addToCart(item: SupplyTransactionItem) {
        android.util.Log.d("VM_DEBUG", "Items antes: ${_uiState.value.cartItems.size}")
        try {
            val updatedList = _uiState.value.cartItems.toMutableList().apply {
                add(item.copy(itemId = java.util.UUID.randomUUID().toString()))
            }
            val newTotal = updatedList.sumOf { it.quantity * it.costPrice }
            _uiState.update { it.copy(cartItems = updatedList, totalInvestment = newTotal) }
            vibrationManager.vibrateSuccess()
        } catch (e: Exception) {
            android.util.Log.e("CARRITO_ERROR", "Error: ${e.message}")
        }
    }

    fun setStoreId(id: String) {
        if (id.isNotEmpty()) {
            currentStoreId = id
            android.util.Log.d("SUPPLY_VM", "ID Guardado: $currentStoreId")
        }
    }

    fun finishPurchase(manualStoreId: String = "") {
        val finalId = manualStoreId.ifEmpty { currentStoreId.ifEmpty { storeId } }
        val state = _uiState.value

        if (finalId.isEmpty()) {
            android.util.Log.e("SUPPLY_VM", "error iddddd")
            return
        }

        if (state.cartItems.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val transaction = SupplyTransaction(
                transactionId = UUID.randomUUID().toString(),
                storeId = finalId,
                date = Instant.now().toString(),
                totalCost = state.totalInvestment,
                items = state.cartItems.map { item ->
                    item.copy(
                        presentationId = item.presentationId,
                        inventoryId = item.inventoryId
                    )
                }
            )

            confirmPurchaseUseCase(transaction).onSuccess {
                vibrationManager.vibrateSuccess()
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { e ->
                vibrationManager.vibrateError()
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun removeFromCart(itemId: String) {
        val updatedList = _uiState.value.cartItems.filter { it.itemId != itemId }
        _uiState.update { it.copy(
            cartItems = updatedList,
            totalInvestment = updatedList.sumOf { it.quantity * it.costPrice }
        ) }
    }
}