package com.softgenix.abastock.core.di

import android.content.Context
import androidx.room.Room
import com.softgenix.abastock.core.database.AbastockDatabase
import com.softgenix.abastock.core.database.dao.InventoryDao
import com.softgenix.abastock.core.database.dao.PurchaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAbastockDatabase(
        @ApplicationContext context: Context
    ): AbastockDatabase {
        return Room.databaseBuilder(
            context,
            AbastockDatabase::class.java,
            "abastock_local_db"
        )
            .build()
    }

    @Provides
    fun provideInventoryDao(database: AbastockDatabase): InventoryDao = database.inventoryDao()


    @Provides
    fun providePurchaseDao(database: AbastockDatabase): PurchaseDao = database.purchaseDao()

}