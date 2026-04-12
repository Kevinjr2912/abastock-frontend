/*
package com.softgenix.abastock.features.purchases.domain.usecases


import com.softgenix.abastock.features.purchases.domain.entities.Purchase
import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import javax.inject.Inject

class CreatePurchaseUseCase @Inject constructor(
    private val repository: PurchaseRepository
) {
    suspend operator fun invoke(purchase: Purchase): Result<Unit> {
        if (purchase.purchaseItems.isEmpty()) {
            return Result.failure(Exception("El carrito está vacío"))
        }
        return repository.createPurchase(purchase)
    }
}

*/
