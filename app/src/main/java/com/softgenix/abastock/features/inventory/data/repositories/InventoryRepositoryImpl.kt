package com.softgenix.abastock.features.inventory.data.repositories

import android.content.Context
import com.google.gson.Gson
import com.softgenix.abastock.features.inventory.data.datasources.remote.api.InventoryApi
import com.softgenix.abastock.features.inventory.data.datasources.remote.mapper.toDomain
import com.softgenix.abastock.features.inventory.data.datasources.remote.mapper.toDto
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.entities.NewProduct
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import com.softgenix.abastock.features.inventory.domain.entities.Brand
import com.softgenix.abastock.features.inventory.domain.entities.Category
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

import javax.inject.Inject

class InventoryRepositoryImpl @Inject constructor(
    private val api: InventoryApi,
    @ApplicationContext private val context: Context
) : InventoryRepository {

    override suspend fun getInventory(storeId: String): Result<List<InventoryItem>> {
        return try {
            val response = api.getInventory(storeId)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchInventory(storeId: String, query: String): Result<List<InventoryItem>> {
        return try {
            val response = api.searchInventory(storeId, query)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun scanProduct(storeId: String, barcode: String): Result<ScannedProduct?> {
        android.util.Log.d("SCANNER_REPO", "Iniciando petición -> storeId: $storeId, barcode: $barcode")

        return try {
            val response = api.scanProduct(storeId, barcode)

            android.util.Log.d("SCANNER_REPO", "Respuesta Exitosa (200 OK): $response")

            val domainProduct = response.toDomain()
            android.util.Log.d("SCANNER_REPO", "Mapeo exitoso: $domainProduct")

            Result.success(domainProduct)

        } catch (e: retrofit2.HttpException) {
            val code = e.code()
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("SCANNER_REPO", "Error HTTP $code: $errorBody")

            if (code == 404) Result.success(null)
            else Result.failure(e)

        } catch (e: Exception) {
            android.util.Log.e("SCANNER_REPO", "EXCEPCIÓN CRÍTICA: ${e.message}")
            e.printStackTrace()
            Result.failure(e)
        }
    }


    override suspend fun getBrands(): Result<List<Brand>> = try {
        Result.success(api.getBrands().map { it.toDomain() })
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun getCategories(): Result<List<Category>> = try {
        Result.success(api.getCategories().map { it.toDomain() })
    } catch (e: Exception) { Result.failure(e) }

    override suspend fun createProduct(storeId: String, product: NewProduct): Result<Unit> {
        return try {
            val gson = Gson()
            val jsonString = gson.toJson(product.toDto(storeId))
            val dataPart = jsonString.toRequestBody("application/json".toMediaTypeOrNull())

            val imagePart = product.imageUri?.let { uriString ->
                val uri = android.net.Uri.parse(uriString)
                val file = uriToFile(context, uri) ?: return@let null
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

                // el name que debe coincidir con el back
                MultipartBody.Part.createFormData("image", file.name, requestFile)
            }

            //agrego el missmatch porque no es multipart
            val response = api.createProduct(dataPart, imagePart)

            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string()
                android.util.Log.e("CREATE_PRODUCT", "ERROR: $errorBody")
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            android.util.Log.e("CREATE_PRODUCT", "EXCEPCIÓN: ${e.message}")
            Result.failure(e)
        }
    }

    // funcion helper para convertir un uri a file ( ACA ESTA SUCIO PERO GGS JSAJS)
    private fun uriToFile(context: Context, uri: android.net.Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)
            inputStream.use { input ->
                tempFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            null
        }
    }
}