package ru.shevrus.roomdbapp.domain.usecase

import ru.shevrus.roomdbapp.domain.repository.ProductRepository

class SyncProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): Result<Unit> =
        repository.syncProducts()
}