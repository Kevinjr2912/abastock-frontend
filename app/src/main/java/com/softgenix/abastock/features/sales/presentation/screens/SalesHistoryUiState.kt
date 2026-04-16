package com.softgenix.abastock.features.sales.presentation.screens

import com.softgenix.abastock.features.sales.domain.entities.DailySalesData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class SalesHistoryUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val salesData: DailySalesData? = null,
    val currentDate: String = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
)