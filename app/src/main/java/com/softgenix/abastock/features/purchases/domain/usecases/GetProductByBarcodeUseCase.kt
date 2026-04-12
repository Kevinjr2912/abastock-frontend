package com.softgenix.abastock.features.purchases.domain.usecases

import com.softgenix.abastock.features.purchases.domain.repositories.PurchaseRepository
import javax.inject.Inject

class GetProductByBarcodeUseCase @Inject constructor(
    private val repository: PurchaseRepository
) {
    suspend operator fun invoke(barcode: String) = repository.getProductByBarcode(barcode)
}