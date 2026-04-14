package com.softgenix.abastock.core.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "purchases_history")
data class LocalPurchaseEntity(
    @PrimaryKey val purchaseId: String,
    val storeId: String,
    val date: String,
    val totalCost: Double,
    val itemsSummary: String
)