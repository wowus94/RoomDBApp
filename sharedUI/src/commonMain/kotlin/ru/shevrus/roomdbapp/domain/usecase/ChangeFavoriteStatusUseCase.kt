package ru.shevrus.roomdbapp.domain.usecase

import ru.shevrus.roomdbapp.domain.repository.ProductRepository

class ChangeFavoriteStatusUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(productId: Long, isFavorite: Boolean) {
        repository.toggleFavorite(productId, isFavorite)
    }
}