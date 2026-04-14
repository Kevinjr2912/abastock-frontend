package com.softgenix.abastock.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.softgenix.abastock.core.database.entities.LocalInventoryEntity


@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<LocalInventoryEntity>)

    @Query("SELECT * FROM local_inventory")
    suspend fun getAllInventory(): List<LocalInventoryEntity>

    @Query("SELECT * FROM local_inventory WHERE barcode = :barcode LIMIT 1")
    suspend fun getProductByBarcode(barcode: String): LocalInventoryEntity?

    @Query("DELETE FROM local_inventory")
    suspend fun clearInventory()
}