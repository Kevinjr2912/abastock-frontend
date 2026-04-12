package com.softgenix.abastock.features.purchases.data.di

import com.softgenix.abastock.features.purchases.data.repositories.PurchaseRepositoryImpl
import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import com.softgenix.abastock.features.purchases.domain.repositories.SupplyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PurchasesRepositoryModule {
    @Binds
    abstract fun bindPurchaseRepository(
        impl: PurchaseRepositoryImpl
    ): PurchaseRepository

    @Binds
    @Singleton
    abstract fun bindSupplyRepository(
        impl: PurchaseRepositoryImpl
    ): SupplyRepository

}
