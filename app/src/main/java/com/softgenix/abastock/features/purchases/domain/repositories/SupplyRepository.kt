package com.softgenix.abastock.features.purchases.domain.repositories

import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransaction

interface SupplyRepository {
    suspend fun processFinalPurchase(transaction: SupplyTransaction): Result<Unit>
}