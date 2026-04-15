package com.softgenix.abastock.features.purchases.presentation.viewmodels

import android.util.Log // 🔥 IMPORTANTE
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct
import com.softgenix.abastock.features.inventory.domain.usecases.ScanProductUseCase
import com.softgenix.abastock.features.purchases.domain.entities.Purchase
import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem
import com.softgenix.abastock.features.purchases.domain.usecases.GetProductByBarcodeUseCase
import com.softgenix.abastock.features.purchases.domain.usecases.SavePurchaseUseCase
import com.softgenix.abastock.features.purchases.presentation.screens.PurchaseUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PurchaseViewModel @Inject constructor(
    private val getProductByBarcodeUseCase: GetProductByBarcodeUseCase,
    private val savePurchaseUseCase: SavePurchaseUseCase,
    private val scanProductUseCase: ScanProductUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PurchaseUiState())
    val uiState = _uiState.asStateFlow()
    private val _cartItems = MutableStateFlow<List<PurchaseItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow()

    val totalPurchase: Double
        get() = _uiState.value.cartItems.sumOf { it.quantity * it.costPrice }

    private var isProcessingBarcode = false

    fun onBarcodeScanned(
        barcode: String,
        onExists: (ScannedProduct) -> Unit,
        onNotFound: (String) -> Unit
    ) {
        if (isProcessingBarcode) return
        isProcessingBarcode = true

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            scanProductUseCase(barcode).fold(
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false) }

                    val errorMsg = error.message ?: ""
                    if (errorMsg.contains("404") || errorMsg.contains("400")) {
                        onNotFound(barcode)
                    } else {
                        _uiState.update { it.copy(errorMessage = "Error de red: $errorMsg") }
                    }
                },
                onSuccess = { product ->
                    _uiState.update { it.copy(isLoading = false) }
                    if (product != null) {
                        onExists(product)
                    } else {
                        onNotFound(barcode)
                    }
                }
            )

            delay(1500)
            isProcessingBarcode = false
        }
    }

    fun checkProduct(
        barcode: String,
        onExists: (PurchaseItem) -> Unit,
        onNotFound: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            getProductByBarcodeUseCase(barcode).fold(
                onSuccess = { item ->
                    _uiState.update { it.copy(isLoading = false) }
                    if (item != null) onExists(item) else onNotFound(barcode)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                    onNotFound(barcode)
                }
            )
        }
    }

    fun addItemToCart(item: PurchaseItem) {
        _uiState.update { state ->
            state.copy(cartItems = state.cartItems + item)
        }
    }

    fun finalizePurchase(storeId: String) {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.cartItems.isEmpty()) return@launch

            _uiState.update { it.copy(isLoading = true) }

            val purchase = Purchase(
                id = UUID.randomUUID().toString(),
                storeId = storeId,
                items = state.cartItems,
                totalCost = totalPurchase,
                date = Instant.now().toString()
            )

            savePurchaseUseCase(purchase).fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true, cartItems = emptyList()) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }
}