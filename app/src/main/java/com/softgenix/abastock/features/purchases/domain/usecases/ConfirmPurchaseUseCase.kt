package com.softgenix.abastock.features.purchases.domain.usecases

import com.softgenix.abastock.features.purchases.domain.entities.SupplyTransaction
import com.softgenix.abastock.features.purchases.domain.repositories.SupplyRepository
import javax.inject.Inject

class ConfirmPurchaseUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    suspend operator fun invoke(transaction: SupplyTransaction): Result<Unit> {

        if (transaction.items.isEmpty()) {
            return Result.failure(Exception("No puedes finalizar una compra sin productos"))
        }
        if (transaction.totalCost <= 0) {
            return Result.failure(Exception("El costo total debe ser mayor a cero"))
        }

        return repository.processFinalPurchase(transaction)
    }
}