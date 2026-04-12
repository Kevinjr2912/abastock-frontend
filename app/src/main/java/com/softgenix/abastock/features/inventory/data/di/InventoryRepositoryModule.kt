package com.softgenix.abastock.features.inventory.data.di

import com.softgenix.abastock.features.inventory.data.repositories.InventoryRepositoryImpl
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class InventoryRepositoryModule {

    @Binds
    abstract fun bindInventoryRepository(
        inventoryRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository
}