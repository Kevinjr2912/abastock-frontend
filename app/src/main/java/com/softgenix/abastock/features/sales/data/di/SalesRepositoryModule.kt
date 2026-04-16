package com.softgenix.abastock.features.sales.data.di

import com.softgenix.abastock.features.sales.data.repositories.SalesRepositoryImpl
import com.softgenix.abastock.features.sales.domain.repositories.SalesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SalesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSalesRepository(
        impl: SalesRepositoryImpl
    ): SalesRepository

}