package ru.shevrus.roomdbapp.domain.usecase

import ru.shevrus.roomdbapp.domain.repository.ProductRepository

class SyncProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(limit: Int, skip: Int): Result<Int> =
        repository.syncProducts(limit, skip)
}