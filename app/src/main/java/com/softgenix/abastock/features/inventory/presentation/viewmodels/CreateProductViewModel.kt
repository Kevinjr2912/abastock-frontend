package com.softgenix.abastock.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.hardware.domain.VibrationManager
import com.softgenix.abastock.features.inventory.domain.entities.Brand
import com.softgenix.abastock.features.inventory.domain.entities.Category
import com.softgenix.abastock.features.inventory.domain.entities.NewBarcode
import com.softgenix.abastock.features.inventory.domain.entities.NewPresentation
import com.softgenix.abastock.features.inventory.domain.entities.NewProduct
import com.softgenix.abastock.features.inventory.domain.usecases.CreateInventoryUseCase
import com.softgenix.abastock.features.inventory.domain.usecases.CreateProductUseCase
import com.softgenix.abastock.features.inventory.domain.usecases.GetCreateProductDataUseCase
import com.softgenix.abastock.features.inventory.presentation.screens.CreateProductUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val getInitialDataUseCase: GetCreateProductDataUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val vibrationManager: VibrationManager,
    private val createInventoryUseCase: CreateInventoryUseCase,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateProductUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getInitialDataUseCase().onSuccess { (brands, categories) ->
                _uiState.update {
                    it.copy(
                        brands = brands,
                        categories = categories,
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }


    fun onNameChange(name: String) = _uiState.update { it.copy(name = name) }
    fun onBrandSelect(brand: Brand) = _uiState.update { it.copy(selectedBrand = brand) }
    fun onCategorySelect(category: Category) =
        _uiState.update { it.copy(selectedCategory = category) }

    fun onPriceChange(price: String) = _uiState.update { it.copy(salePrice = price) }

    fun onValueChange(value: String) = _uiState.update { it.copy(value = value) }
    fun onUnitChange(unit: String) = _uiState.update { it.copy(unit = unit) }

    fun onImageSelected(uri: android.net.Uri) {
        _uiState.update { it.copy(selectedImageUri = uri) }
    }

    fun saveProduct(barcode: String) {
        val state = _uiState.value
        if (state.name.isBlank()) {
            vibrationManager.vibrateError()
            _uiState.update { it.copy(error = "El nombre es obligatorio") }
            return
        }

        if (state.selectedBrand == null || state.selectedCategory == null) {
            vibrationManager.vibrateError()
            _uiState.update { it.copy(error = "Selecciona marca y categoría") }
            return
        }

        if (state.selectedImageUri == null) {
            vibrationManager.vibrateError()
            _uiState.update { it.copy(error = "Por favor selecciona una imagen de la galería") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val presentationId = UUID.randomUUID().toString()

            val newProduct = NewProduct(
                productId = UUID.randomUUID().toString(),
                name = state.name,
                brandId = state.selectedBrand.id,
                categoryId = state.selectedCategory.id,
                imageUri = state.selectedImageUri?.toString(),
                presentation = NewPresentation(
                    presentationId = presentationId,
                    value = state.value.toIntOrNull() ?: 1,
                    unit = state.unit,
                    salePrice = 1.0,
                    barcode = NewBarcode(
                        barcodeId = UUID.randomUUID().toString(),
                        code = barcode
                    )
                )
            )
            createProductUseCase(newProduct).onSuccess {
                val inventoryId = UUID.randomUUID().toString()
                val storeId = tokenManager.getStoreId()

                createInventoryUseCase(
                    inventoryId = inventoryId,
                    storeId = storeId,
                    presentationId = presentationId
                ).onSuccess {
                    vibrationManager.vibrateSuccess()
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }

                }.onFailure { e ->
                    vibrationManager.vibrateError()
                    _uiState.update { it.copy(isLoading = false, error = "Producto creado, pero falló el inventario: ${e.message}") }
                }

            }.onFailure { e ->
                vibrationManager.vibrateError()
                _uiState.update { it.copy(isLoading = false, error = e.message ?: "Error desconocido") }
            }
        }
    }
}