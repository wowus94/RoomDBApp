package ru.shevrus.roomdbapp.presentation

import ru.shevrus.roomdbapp.domain.model.Product

data class ProductsUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)