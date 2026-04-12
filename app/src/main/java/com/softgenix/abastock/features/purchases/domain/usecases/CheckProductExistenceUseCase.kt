package com.softgenix.abastock.features.purchases.domain.usecases

import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem
import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import javax.inject.Inject

class CheckProductExistenceUseCase @Inject constructor(
    private val repository: PurchaseRepository
) {
    suspend operator fun invoke(barcode: String): Result<PurchaseItem?> {
        return repository.getProductByBarcode(barcode)
    }
}