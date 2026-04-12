package com.softgenix.abastock.features.purchases.data.datasources.remote.mapper

import com.softgenix.abastock.features.purchases.data.datasources.remote.models.SupplyTransactionItemDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.SupplyTransactionRequestDto
import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransaction
import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransactionItem

fun SupplyTransaction.toDto() = SupplyTransactionRequestDto(
    purchaseId = this.transactionId,
    storeId = this.storeId,
    purchaseDate = this.date,
    totalCost = this.totalCost,
    purchaseItems = this.items.map { it.toDto() }
)

fun SupplyTransactionItem.toDto() = SupplyTransactionItemDto(
    purchaseItemId = this.itemId,
    presentationId = this.presentationId,
    inventoryId = this.inventoryId,
    quantity = this.quantity,
    costPrice = this.costPrice,
    salePrice = this.salePrice
)