package com.softgenix.abastock.features.inventory.domain.usecases
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class ScanProductUseCase @Inject constructor(
    private val repository: InventoryRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(barcode: String): Result<ScannedProduct?> {
        val session = tokenManager.getSession()
        val storeId = session?.storeId ?: return Result.failure(Exception("No se encontró el ID de la tienda"))

        return repository.scanProduct(storeId, barcode)
    }
}