package com.softgenix.abastock.features.sales.data.datasources.remote.api

import com.softgenix.abastock.features.sales.data.datasources.remote.dto.SaleRequestDto
import com.softgenix.abastock.features.sales.data.datasources.remote.dto.SalesHistoryResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SalesApi {

    @POST("sales")
    suspend fun registerSale(@Body request: SaleRequestDto): Response<Unit>

    @GET("sales")
    suspend fun getSalesHistory(
        @Query("storeId") storeId: String,
        @Query("date") date: String
    ): Response<SalesHistoryResponseDto>
}