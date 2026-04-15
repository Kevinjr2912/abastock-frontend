package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class CreateInventoryUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(inventoryId: String, storeId: String, presentationId: String): Result<Unit> {
        return repository.createInventory(inventoryId, storeId, presentationId)
    }
}