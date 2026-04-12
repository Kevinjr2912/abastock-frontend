package com.softgenix.abastock.features.inventory.data.di

import com.softgenix.abastock.core.di.AbastockRetrofit
import com.softgenix.abastock.core.di.AppRetrofit
import com.softgenix.abastock.features.inventory.data.datasources.remote.api.InventoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InventoryNetworkModule {

    @Provides
    @Singleton
    fun provideInventoryApi(@AppRetrofit retrofit: Retrofit): InventoryApi {
        return retrofit.create(InventoryApi::class.java)
    }
}