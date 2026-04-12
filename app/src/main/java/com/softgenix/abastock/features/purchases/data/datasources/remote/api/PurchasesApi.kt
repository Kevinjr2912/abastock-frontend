package com.softgenix.abastock.features.purchases.data.datasources.remote.api

import com.softgenix.abastock.features.purchases.data.datasources.remote.models.AbastockResponse
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.CreatePurchaseRequestDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.ProductDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.PurchaseRequestDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.PurchaseResponseDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.SupplyResponseDto
import com.softgenix.abastock.features.purchases.data.datasources.remote.models.SupplyTransactionRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PurchasesApi {
    @GET("products/barcode/{barcode}")
    suspend fun getProductByBarcode(@Path("barcode") barcode: String): AbastockResponse<ProductDto>

    @POST("purchases")
    suspend fun savePurchase(@Body purchaseRequest: PurchaseRequestDto): AbastockResponse<Unit>

    @POST("purchases")
    suspend fun createPurchase(
        @Body request: SupplyTransactionRequestDto
    ): Response<SupplyResponseDto>

}