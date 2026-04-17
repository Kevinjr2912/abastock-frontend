package com.softgenix.abastock.features.store.data.datasources.remote.repositories

import com.softgenix.abastock.features.store.data.datasources.remote.api.StoreApi
import com.softgenix.abastock.features.store.data.datasources.remote.mapper.toDto
import com.softgenix.abastock.features.store.domain.entities.Store
import com.softgenix.abastock.features.store.domain.repositories.StoreRepository
import javax.inject.Inject

class StoreRepositoryImpl @Inject constructor(
    private val api: StoreApi
) : StoreRepository {

    override suspend fun createStore(store: Store): Result<Unit> {
        return try {
            val response = api.createStore(store.toDto())

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                Result.failure(Exception("HTTP ${response.code()}: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}