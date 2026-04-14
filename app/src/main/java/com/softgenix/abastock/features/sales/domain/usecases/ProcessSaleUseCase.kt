package com.softgenix.abastock.features.sales.domain.usecases

import com.softgenix.abastock.features.sales.domain.entities.SaleTransaction
import com.softgenix.abastock.features.sales.domain.repositories.SalesRepository
import javax.inject.Inject

class ProcessSaleUseCase @Inject constructor(
    private val repository: SalesRepository
) {
    suspend operator fun invoke(transaction: SaleTransaction): Result<Unit> {
        if (transaction.totalAmount <= 0) {
            return Result.failure(Exception("El total de la venta debe ser mayor a 0"))
        }
        if (transaction.items.isEmpty()) {
            return Result.failure(Exception("No hay productos en la venta"))
        }

        return repository.processSale(transaction)
    }
}