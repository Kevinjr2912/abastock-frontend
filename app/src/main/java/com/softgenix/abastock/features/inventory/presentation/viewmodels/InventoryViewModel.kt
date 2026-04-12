package com.softgenix.abastock.features.inventory.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.hardware.domain.VoiceManager
import com.softgenix.abastock.features.inventory.domain.usecases.GetInventoryUseCase
import com.softgenix.abastock.features.inventory.domain.usecases.SearchInventoryUseCase
import com.softgenix.abastock.features.inventory.presentation.screens.InventoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val getInventoryUseCase: GetInventoryUseCase,
    private val searchInventoryUseCase: SearchInventoryUseCase,
    private val voiceManager: VoiceManager,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val storeId = tokenManager.getSession()?.storeId ?: ""
        if (storeId.isNotEmpty()) {
            loadInventory(storeId)
        } else {
            _uiState.update { it.copy(errorMessage = "No se encontró ID de tienda") }
        }
    }

    fun startVoiceSearch() {
        voiceManager.startListening { text ->
            onSearchQueryChange(text)
        }
    }

    fun loadInventory(storeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getInventoryUseCase(storeId).onSuccess { list ->
                _uiState.update { it.copy(
                    isLoading = false,
                    items = list,
                    filteredItems = list
                ) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
        filterInventory()
    }

    fun onCategorySelected(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
        filterInventory()
    }

    private fun filterInventory() {
        val currentQuery = _uiState.value.searchQuery
        val currentCat = _uiState.value.selectedCategory

        val filtered = _uiState.value.items.filter { item ->
            val matchesQuery = item.productName.contains(currentQuery, ignoreCase = true) ||
                    item.brandName.contains(currentQuery, ignoreCase = true)
            val matchesCat = currentCat == "Todos" || item.categoryName == currentCat
            matchesQuery && matchesCat
        }
        _uiState.update { it.copy(filteredItems = filtered) }
    }
}