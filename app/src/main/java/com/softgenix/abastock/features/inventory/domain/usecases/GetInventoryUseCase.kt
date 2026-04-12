package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class GetInventoryUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(storeId: String): Result<List<InventoryItem>> {
        return repository.getInventory(storeId)
    }
}