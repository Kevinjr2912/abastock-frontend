package com.softgenix.abastock.features.inventory.domain.repositories

import com.softgenix.abastock.features.inventory.domain.entities.Brand
import com.softgenix.abastock.features.inventory.domain.entities.Category
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.entities.NewProduct
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct

interface InventoryRepository {
    suspend fun getInventory(storeId: String): Result<List<InventoryItem>>

    suspend fun searchInventory(storeId: String, query: String): Result<List<InventoryItem>>
    suspend fun scanProduct(storeId: String, barcode: String): Result<ScannedProduct?>

    suspend fun getBrands(): Result<List<Brand>>
    suspend fun getCategories(): Result<List<Category>>

    suspend fun createProduct(storeId: String, product: NewProduct): Result<Unit>
}