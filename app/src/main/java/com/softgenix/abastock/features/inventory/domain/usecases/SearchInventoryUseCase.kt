package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class SearchInventoryUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(storeId: String, query: String): Result<List<InventoryItem>> {
        if (query.isBlank()) return repository.getInventory(storeId)
        return repository.searchInventory(storeId, query)
    }
}