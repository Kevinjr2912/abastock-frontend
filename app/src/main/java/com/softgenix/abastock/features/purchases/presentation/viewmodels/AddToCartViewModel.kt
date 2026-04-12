package com.softgenix.abastock.features.purchases.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.inventory.domain.usecases.ScanProductUseCase
import com.softgenix.abastock.features.purchases.presentation.screens.AddToCartUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddToCartViewModel @Inject constructor(
    private val scanProductUseCase: ScanProductUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddToCartUiState())
    val uiState = _uiState.asStateFlow()

    fun loadProduct(barcode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            scanProductUseCase(barcode).onSuccess { product ->
                _uiState.update {
                    it.copy(
                        product = product,
                        isLoading = false,
                        costPrice = product?.value?.toString() ?: "",
                        salePrice = product?.value?.toString() ?: ""
                    )
                }
            }.onFailure {
                _uiState.update { it.copy(isLoading = false, error = "Error al cargar info") }
            }
        }
    }

    fun onQuantityChange(value: String) = _uiState.update { it.copy(quantity = value) }
    fun onCostChange(value: String) = _uiState.update { it.copy(costPrice = value) }
    fun onSaleChange(value: String) = _uiState.update { it.copy(salePrice = value) }
}