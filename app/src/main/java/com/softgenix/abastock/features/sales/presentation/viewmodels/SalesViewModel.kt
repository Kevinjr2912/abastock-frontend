package com.softgenix.abastock.features.sales.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.hardware.domain.VibrationManager
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem // 🔥 IMPORTANTE: Necesitamos conocer este modelo
import com.softgenix.abastock.features.sales.domain.entities.SaleTransaction
import com.softgenix.abastock.features.sales.domain.entities.SaleTransactionItem
import com.softgenix.abastock.features.sales.domain.usecases.ProcessSaleUseCase
import com.softgenix.abastock.features.sales.presentation.screens.SaleCartItem
import com.softgenix.abastock.features.sales.presentation.screens.SalesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SalesViewModel @Inject constructor(
    private val processSaleUseCase: ProcessSaleUseCase,
    private val tokenManager: TokenManager,
    private val vibrationManager: VibrationManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    fun addInventoryItemToCart(inventoryItem: InventoryItem) {
        val cartItem = SaleCartItem(
            presentationId = inventoryItem.presentationId,
            inventoryId = inventoryItem.inventoryId,
            name = inventoryItem.productName,
            salePrice = inventoryItem.salePrice,
            quantity = 1,
            imageUrl = inventoryItem.imageUri
        )
        addToCart(cartItem)
    }

    fun addToCart(item: SaleCartItem) {
        val currentItems = _uiState.value.cartItems.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.presentationId == item.presentationId }

        if (existingItemIndex != -1) {
            val existing = currentItems[existingItemIndex]
            currentItems[existingItemIndex] = existing.copy(quantity = existing.quantity + item.quantity)
        } else {
            currentItems.add(item)
        }
        vibrationManager.vibrateSuccess()
        _uiState.update { it.copy(cartItems = currentItems) }
    }

    fun increaseQuantity(cartItemId: String) {
        val updatedItems = _uiState.value.cartItems.map {
            if (it.cartItemId == cartItemId) it.copy(quantity = it.quantity + 1) else it
        }
        _uiState.update { it.copy(cartItems = updatedItems) }
    }

    fun decreaseQuantity(cartItemId: String) {
        val updatedItems = _uiState.value.cartItems.mapNotNull {
            if (it.cartItemId == cartItemId) {
                if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else null
            } else it
        }
        _uiState.update { it.copy(cartItems = updatedItems) }
    }

    fun removeFromCart(cartItemId: String) {
        val updatedItems = _uiState.value.cartItems.filter { it.cartItemId != cartItemId }
        _uiState.update { it.copy(cartItems = updatedItems) }
    }

    fun processSale() {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val storeId = tokenManager.getStoreId()

            val transaction = SaleTransaction(
                saleId = UUID.randomUUID().toString(),
                storeId = storeId,
                saleDate = Instant.now().toString(),
                totalAmount = state.totalAmount,
                items = state.cartItems.map { uiItem ->
                    SaleTransactionItem(
                        saleItemId = UUID.randomUUID().toString(),
                        presentationId = uiItem.presentationId,
                        inventoryId = uiItem.inventoryId,
                        quantity = uiItem.quantity,
                        salePrice = uiItem.salePrice,
                        subtotal = uiItem.salePrice * uiItem.quantity
                    )
                }
            )

            processSaleUseCase(transaction).fold(
                onSuccess = {
                    vibrationManager.vibrateSuccess()
                    val finalTotal = state.totalAmount

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            cartItems = emptyList(),
                            lastSaleTotal = finalTotal
                        )
                    }
                },
                onFailure = { error ->
                    vibrationManager.vibrateError()
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }
}