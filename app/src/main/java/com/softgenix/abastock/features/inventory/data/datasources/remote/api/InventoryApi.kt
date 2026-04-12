package com.softgenix.abastock.features.inventory.data.datasources.remote.api


import com.softgenix.abastock.features.inventory.data.datasources.remote.models.BrandDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.CategoryDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.CreateProductRequestDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.InventoryDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.InventoryResponse
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.InventoryScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface InventoryApi {
    @GET("inventories")
    suspend fun getInventory(
        @Query("storeId") storeId: String
    ): List<InventoryDto>


    @GET("inventory/store/{storeId}/search")
    suspend fun searchInventory(
        @Path("storeId") storeId: String,
        @Query("q") query: String
    ): List<InventoryDto>


    @GET("inventories/scan")
    suspend fun scanProduct(
        @Query("storeId") storeId: String,
        @Query("barcode") barcode: String
    ): InventoryScanResponse

    @GET("products/brands")
    suspend fun getBrands(): List<BrandDto>

    @GET("products/categories")
    suspend fun getCategories(): List<CategoryDto>

    @Multipart
    @POST("products")
    suspend fun createProduct(
        @Part("data") productData: RequestBody,
        @Part image: MultipartBody.Part?
    ): retrofit2.Response<Unit>
}
