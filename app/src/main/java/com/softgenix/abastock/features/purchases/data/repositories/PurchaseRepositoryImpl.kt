package com.softgenix.abastock.features.purchases.data.repositories

import com.softgenix.abastock.core.database.dao.PurchaseDao
import com.softgenix.abastock.core.database.entities.LocalPurchaseEntity
import com.softgenix.abastock.features.purchases.data.datasources.remote.api.PurchasesApi
import com.softgenix.abastock.features.purchases.data.datasources.remote.mapper.toDomain
import com.softgenix.abastock.features.purchases.data.datasources.remote.mapper.toDto
import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem
import javax.inject.Inject

import com.softgenix.abastock.features.purchases.domain.entities.Purchase
import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransaction
import com.softgenix.abastock.features.purchases.domain.repositories.SupplyRepository

class PurchaseRepositoryImpl @Inject constructor(
    private val api: PurchasesApi,
    private val purchaseDao: PurchaseDao
) : PurchaseRepository, SupplyRepository {

    override suspend fun getProductByBarcode(barcode: String): Result<PurchaseItem?> {
        return try {
            val response = api.getProductByBarcode(barcode)
            if (response.success) {
                Result.success(response.data.toDomain().copy(barcode = barcode))
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun savePurchase(purchase: Purchase): Result<Unit> {
        return try {
            // se manda a la api
            val response = api.savePurchase(purchase.toDto())

            if (response.success) {
                val summaryText = purchase.items.joinToString(", ") { "${it.quantity}x ${it.name}" }
                val localEntity = LocalPurchaseEntity(
                    purchaseId = purchase.id,
                    storeId = purchase.storeId,
                    date = purchase.date,
                    totalCost = purchase.totalCost,
                    itemsSummary = summaryText
                )
                purchaseDao.insertPurchase(localEntity)
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun processFinalPurchase(transaction: SupplyTransaction): Result<Unit> {
        return try {
            val response = api.createPurchase(transaction.toDto())

            if (response.isSuccessful) {
                val summaryText = transaction.items.joinToString(", ") { item ->
                    val nombreReal = item.name.ifEmpty { "ENVIANDOOOOOOOO" }
                    "${item.quantity}x $nombreReal"
                }

                val localEntity = LocalPurchaseEntity(
                    purchaseId = transaction.transactionId,
                    storeId = transaction.storeId,
                    date = transaction.date,
                    totalCost = transaction.totalCost,
                    itemsSummary = summaryText
                )

                purchaseDao.insertPurchase(localEntity)
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}