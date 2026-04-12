package com.softgenix.abastock.features.store.di

import com.softgenix.abastock.core.di.AbastockRetrofit
import com.softgenix.abastock.core.di.AppRetrofit
import com.softgenix.abastock.features.store.data.datasources.remote.api.StoreApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoreModule {
    @Provides
    @Singleton
    fun provideStoreApi(@AppRetrofit retrofit: Retrofit): StoreApi {
        return retrofit.create(StoreApi::class.java)
    }
}