package com.softgenix.abastock.features.sales.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.sales.domain.usecases.GetSalesHistoryUseCase
import com.softgenix.abastock.features.sales.presentation.screens.SalesHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SalesHistoryViewModel @Inject constructor(
    private val getSalesHistoryUseCase: GetSalesHistoryUseCase,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SalesHistoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val storeId = tokenManager.getStoreId()
            val date = _uiState.value.currentDate

            getSalesHistoryUseCase(storeId, date).fold(
                onSuccess = { data ->
                    _uiState.update { it.copy(isLoading = false, salesData = data) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            )
        }
    }
}