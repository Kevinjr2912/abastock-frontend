package com.softgenix.abastock.features.purchases.data.di

import com.softgenix.abastock.core.di.AbastockRetrofit
import com.softgenix.abastock.core.di.AppRetrofit
import com.softgenix.abastock.features.purchases.data.datasources.remote.api.PurchasesApi
import com.softgenix.abastock.features.purchases.data.repositories.PurchaseRepositoryImpl
import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PurchasesNetworkModule {
    @Provides
    @Singleton
    fun providePurchasesApi(@AppRetrofit retrofit: Retrofit): PurchasesApi {
        return retrofit.create(PurchasesApi::class.java)
    }
}