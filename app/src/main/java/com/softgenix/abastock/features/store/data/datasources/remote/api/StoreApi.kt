package com.softgenix.abastock.features.store.data.datasources.remote.api

import com.softgenix.abastock.features.store.data.datasources.remote.models.StoreDto
import com.softgenix.abastock.features.store.domain.entities.Store
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface StoreApi {
    @POST("stores")
    suspend fun createStore(@Body store: StoreDto): Response<Unit>
}