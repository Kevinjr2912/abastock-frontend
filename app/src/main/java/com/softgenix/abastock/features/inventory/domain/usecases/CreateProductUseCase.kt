package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.features.inventory.domain.entities.NewProduct
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject


class CreateProductUseCase @Inject constructor(
    private val repository: InventoryRepository,
    private val tokenManager: com.softgenix.abastock.core.data.local.TokenManager
) {
    suspend operator fun invoke(product: NewProduct): Result<Unit> {
        val storeId = tokenManager.getSession()?.storeId
            ?: return Result.failure(Exception("No se encontró storeId en la sesión"))

        return repository.createProduct(storeId, product)
    }
}