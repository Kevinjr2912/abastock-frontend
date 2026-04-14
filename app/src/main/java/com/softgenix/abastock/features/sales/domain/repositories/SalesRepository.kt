package com.softgenix.abastock.features.sales.domain.repositories

import com.softgenix.abastock.features.sales.domain.entities.DailySalesData
import com.softgenix.abastock.features.sales.domain.entities.SaleTransaction

interface SalesRepository {
    suspend fun processSale(transaction: SaleTransaction): Result<Unit>
    suspend fun getSalesHistory(storeId: String, date: String): Result<DailySalesData>
}