package com.softgenix.abastock.features.purchases.domain.usecases

import com.softgenix.abastock.features.purchases.domain.entities.Purchase
import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import javax.inject.Inject

class SavePurchaseUseCase @Inject constructor(
    private val repository: PurchaseRepository
) {
    suspend operator fun invoke(purchase: Purchase) = repository.savePurchase(purchase)
}