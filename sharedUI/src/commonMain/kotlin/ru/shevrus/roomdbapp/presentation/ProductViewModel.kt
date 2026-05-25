package ru.shevrus.roomdbapp.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.shevrus.roomdbapp.domain.usecase.GetProductsUseCase
import ru.shevrus.roomdbapp.domain.usecase.SyncProductsUseCase
import ru.shevrus.roomdbapp.presentation.util.UiErrorTranslator

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val syncProductsUseCase: SyncProductsUseCase,
    errorTranslator: UiErrorTranslator
) : BaseViewModel(errorTranslator) {

    val uiState: StateFlow<ProductsUiState> = combine(
        getProductsUseCase(),
        isLoading,
        errorMessage
    ) { products, loading, error ->
        ProductsUiState(products = products, isLoading = loading, errorMessage = error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductsUiState())

    init {
        loadAndSync()
    }

    fun loadAndSync() {
        safeLaunch(
            block = { syncProductsUseCase() }
        )
    }

}