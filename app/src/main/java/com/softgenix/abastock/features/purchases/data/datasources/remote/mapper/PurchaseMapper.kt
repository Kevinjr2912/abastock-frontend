package com.softgenix.abastock.features.purchases.data.datasources.remote.mapper

import com.softgenix.abastock.features.purchases.data.datasources.remote.models.ProductDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.PurchaseItemRequestDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.PurchaseRequestDto
import com.softgenix.abastock.features.purchases.domain.entities.Purchase
import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem

fun ProductDto.toDomain(): PurchaseItem {
    return PurchaseItem(
        presentationId = this.presentationId,
        name = "${this.name} ${this.brandName}",
        quantity = 0.0,
        costPrice = 0.0,
        salePrice = this.salePrice,
        barcode = ""
    )
}

//de aoo a api
fun Purchase.toDto(): PurchaseRequestDto {
    return PurchaseRequestDto(
        storeId = this.storeId,
        totalCost = this.totalCost,
        items = this.items.map { it.toItemDto() }
    )
}

fun PurchaseItem.toItemDto(): PurchaseItemRequestDto {
    return PurchaseItemRequestDto(
        presentationId = this.presentationId,
        quantity = this.quantity,
        costPrice = this.costPrice
    )
}