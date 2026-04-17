package com.softgenix.abastock.features.store.data.datasources.remote.mapper

import com.softgenix.abastock.features.store.data.datasources.remote.models.StoreDto
import com.softgenix.abastock.features.store.domain.entities.Store

fun Store.toDto(): StoreDto {
    return StoreDto(
        storeId = this.id,
        name = this.name,
        userId = this.userId
    )
}