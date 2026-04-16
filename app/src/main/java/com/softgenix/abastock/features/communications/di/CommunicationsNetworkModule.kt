package com.softgenix.abastock.features.communications.di

import com.softgenix.abastock.core.di.AppRetrofit
import com.softgenix.abastock.features.communications.data.datasources.remote.api.CommunicationsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommunicationsNetworkModule {

    @Provides
    @Singleton
    fun provideCommunicationsApi(@AppRetrofit retrofit: Retrofit): CommunicationsApi =
        retrofit.create(CommunicationsApi::class.java)
}