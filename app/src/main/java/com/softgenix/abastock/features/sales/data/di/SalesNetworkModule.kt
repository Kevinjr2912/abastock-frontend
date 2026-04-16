package com.softgenix.abastock.features.sales.data.di

import com.softgenix.abastock.core.di.AppRetrofit
import com.softgenix.abastock.features.sales.data.datasources.remote.api.SalesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SalesNetworkModule {
    @Provides
    @Singleton
    fun provideSalesApi(@AppRetrofit retrofit: Retrofit): SalesApi {
        return retrofit.create(SalesApi::class.java)
    }
}