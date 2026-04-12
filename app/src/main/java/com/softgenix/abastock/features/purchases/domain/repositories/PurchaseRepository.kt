package com.softgenix.abastock.features.purchases.domain.repositories

import com.softgenix.abastock.features.purchases.domain.entities.Purchase
import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem
import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransaction

interface PurchaseRepository {

    suspend fun getProductByBarcode(barcode: String): Result<PurchaseItem?>

    suspend fun savePurchase(purchase: Purchase): Result<Unit>
    suspend fun processFinalPurchase(transaction: SupplyTransaction): Result<Unit>
}