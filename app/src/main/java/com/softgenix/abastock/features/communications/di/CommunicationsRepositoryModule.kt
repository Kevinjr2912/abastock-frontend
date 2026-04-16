package com.softgenix.abastock.features.communications.di

import com.softgenix.abastock.features.communications.data.repositories.CommunicationsRepositoryImpl
import com.softgenix.abastock.features.communications.domain.repositories.CommunicationsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommunicationsRepositoryModule {

    @Binds
    abstract fun bindCommunicationsRepository(
        impl: CommunicationsRepositoryImpl
    ): CommunicationsRepository
}