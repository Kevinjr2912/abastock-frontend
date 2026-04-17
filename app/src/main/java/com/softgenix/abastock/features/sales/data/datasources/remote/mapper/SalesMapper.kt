package com.softgenix.abastock.features.sales.data.datasources.remote.mapper

import com.softgenix.abastock.features.sales.data.datasources.remote.dto.*
import com.softgenix.abastock.features.sales.domain.entities.*

fun SaleTransaction.toDto(): SaleRequestDto {
    return SaleRequestDto(
        saleId = this.saleId,
        storeId = this.storeId,
        saleDate = this.saleDate,
        totalAmount = this.totalAmount,
        saleItems = this.items.map { it.toDto() }
    )
}

fun SaleTransactionItem.toDto(): SaleItemRequestDto {
    return SaleItemRequestDto(
        saleItemId = this.saleItemId,
        presentationId = this.presentationId,
        inventoryId = this.inventoryId,
        quantity = this.quantity,
        salePrice = this.salePrice,
        subtotal = this.subtotal
    )
}

fun SalesHistoryResponseDto.toDomain(): DailySalesData {
    return DailySalesData(
        totalRevenue = this.summary.totalRevenue,
        transactionCount = this.summary.transactionCount,
        salesHistory = this.sales.map { it.toDomain() }
    )
}

fun SaleRecordDto.toDomain(): SaleHistoryRecord {
    return SaleHistoryRecord(
        saleId = this.saleId,
        time = this.time,
        totalAmount = this.totalAmount,
        totalItems = this.totalItems,
        items = this.items.map { it.toDomain() }
    )
}

fun SaleItemDto.toDomain(): SaleHistoryItem {
    return SaleHistoryItem(
        productName = this.productName,
        quantity = this.quantity,
        subtotal = this.subtotal,
        imageUrl = this.imageUrl
    )
}