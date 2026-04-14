package com.softgenix.abastock.core.database.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "local_inventory")
data class LocalInventoryEntity(
    @PrimaryKey val inventoryId: String,
    val presentationId: String,
    val productName: String,
    val brandName: String,
    val categoryName: String,
    val currentStock: Double,
    val salePrice: Double,
    val imageUrl: String?,
    val barcode: String,
    val unit: String
)