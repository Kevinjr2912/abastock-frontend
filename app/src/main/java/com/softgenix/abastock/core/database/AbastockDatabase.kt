package com.softgenix.abastock.core.database

import androidx.room.Database
import androidx.room.RoomDatabase


import com.softgenix.abastock.core.database.entities.LocalInventoryEntity
import com.softgenix.abastock.core.database.entities.LocalPurchaseEntity
import com.softgenix.abastock.core.database.dao.InventoryDao
import com.softgenix.abastock.core.database.dao.PurchaseDao

@Database(
    entities = [
        LocalInventoryEntity::class,
        LocalPurchaseEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AbastockDatabase : RoomDatabase() {
    abstract fun inventoryDao(): InventoryDao
    abstract fun purchaseDao(): PurchaseDao
}
