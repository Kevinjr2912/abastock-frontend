package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.domain.entities.ScannedProduct
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import javax.inject.Inject

class GetInventoryUseCase @Inject constructor(
    private val repository: InventoryRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(storeId: String): Result<List<InventoryItem>> {
        val globalStoreId = tokenManager.getStoreId()
        return repository.getInventory(globalStoreId)
    }
}