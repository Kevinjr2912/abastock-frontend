package com.softgenix.abastock.features.sales.data.repositories

import com.softgenix.abastock.features.sales.data.datasources.remote.api.SalesApi
import com.softgenix.abastock.features.sales.data.datasources.remote.mapper.toDomain
import com.softgenix.abastock.features.sales.data.datasources.remote.mapper.toDto
import com.softgenix.abastock.features.sales.domain.entities.DailySalesData
import com.softgenix.abastock.features.sales.domain.entities.SaleTransaction
import com.softgenix.abastock.features.sales.domain.repositories.SalesRepository
import javax.inject.Inject

class SalesRepositoryImpl @Inject constructor(
    private val api: SalesApi
) : SalesRepository {

    override suspend fun processSale(transaction: SaleTransaction): Result<Unit> {
        return try {

            val response = api.registerSale(transaction.toDto())

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido al procesar venta"
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSalesHistory(storeId: String, date: String): Result<DailySalesData> {
        return try {
            val response = api.getSalesHistory(storeId, date)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.toDomain())
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error al obtener historial"
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}