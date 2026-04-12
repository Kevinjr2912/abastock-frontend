package com.softgenix.abastock.features.inventory.domain.usecases

import com.softgenix.abastock.features.inventory.data.datasources.remote.models.BrandDto
import com.softgenix.abastock.features.inventory.data.datasources.remote.models.CategoryDto
import com.softgenix.abastock.features.inventory.domain.entities.Brand
import com.softgenix.abastock.features.inventory.domain.entities.Category
import com.softgenix.abastock.features.inventory.domain.repositories.InventoryRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetCreateProductDataUseCase @Inject constructor(
    private val repository: InventoryRepository
) {
    suspend operator fun invoke(): Result<Pair<List<Brand>, List<Category>>> = kotlinx.coroutines.coroutineScope {
        try {

            val brandsDeferred = async { repository.getBrands() }
            val categoriesDeferred = async { repository.getCategories() }

            val brands = brandsDeferred.await().getOrThrow()
            val categories = categoriesDeferred.await().getOrThrow()

            Result.success(Pair(brands, categories))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}