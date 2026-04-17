package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.inventory.domain.entities.NewProduct
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject


class CreateProductUseCase @Inject constructor(
    private val repository: InventoryRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(product: NewProduct): Result<Unit> {
        val globalStoreId = tokenManager.getStoreId()
        return repository.createProduct(storeId = globalStoreId, product = product)
    }
}