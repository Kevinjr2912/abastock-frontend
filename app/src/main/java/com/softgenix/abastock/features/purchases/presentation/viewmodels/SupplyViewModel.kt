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
    fun finishPurchase(storeId: String) {
        viewModelScope.launch {
            val currentItems = _uiState.value.cartItems
            if (currentItems.isEmpty()) return@launch

            _uiState.update { it.copy(isLoading = true, error = null) }

            val transaction = SupplyTransaction(
                transactionId = java.util.UUID.randomUUID().toString(),
                storeId = storeId,
                date = java.time.Instant.now().toString(),
                totalCost = currentItems.sumOf { it.quantity * it.costPrice },
                items = currentItems.map { item ->
                    SupplyTransactionItem(
                        itemId = java.util.UUID.randomUUID().toString(),
                        presentationId = item.presentationId,
                        inventoryId = item.inventoryId,
                        quantity = item.quantity.toInt(),
                        costPrice = item.costPrice,
                        salePrice = item.salePrice
                    )
                }
            )
            android.util.Log.d("DEBUG_ABA_BACKEND", "PurchaseID: ${transaction.transactionId}")

            transaction.items.forEachIndexed { index, item ->
                if (item.presentationId.isBlank()) {
                    android.util.Log.e("DEBUG_ABA_BACKEND", " El Item $index tiene presentationId VACÍO")
                }
                if (item.inventoryId.isBlank()) {
                    android.util.Log.e("DEBUG_ABA_BACKEND", " El Item $index tiene inventoryId VACÍO")
                }
                android.util.Log.d("DEBUG_ABA_BACKEND", "Item[$index] -PresID: '${item.presentationId}', InvID: '${item.inventoryId}'")
            }

            confirmPurchaseUseCase(transaction).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { error ->
                    android.util.Log.e("DEBUG_ABA_BACKEND", "Error recibido del Back: ${error.message}")
                    _uiState.update { it.copy(isLoading = false, error= error.message) }
                }
            )
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