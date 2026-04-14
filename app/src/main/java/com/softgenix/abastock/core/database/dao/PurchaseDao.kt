package com.softgenix.abastock.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.softgenix.abastock.core.database.entities.LocalPurchaseEntity

@Dao

interface PurchaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPurchase(purchase: LocalPurchaseEntity)

    @Query("SELECT * FROM purchases_history WHERE date >= :startDate")
    suspend fun getPurchasesFromDate(startDate: String): List<LocalPurchaseEntity>

    @Query("SELECT * FROM purchases_history")
    suspend fun getAllPurchases(): List<LocalPurchaseEntity>
}