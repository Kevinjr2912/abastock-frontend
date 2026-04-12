package com.softgenix.abastock.features.sales.domain.usecases

import com.softgenix.abastock.features.sales.domain.entities.DailySalesData
import com.softgenix.abastock.features.sales.domain.repositories.SalesRepository
import javax.inject.Inject

class GetSalesHistoryUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(storeId: String, date: String): Result<DailySalesData> {
        return repository.getSalesHistory(storeId, date)
    }
}