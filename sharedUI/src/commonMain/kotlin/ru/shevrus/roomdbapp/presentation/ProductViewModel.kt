package ru.shevrus.roomdbapp.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import ru.shevrus.roomdbapp.domain.usecase.ChangeFavoriteStatusUseCase
import ru.shevrus.roomdbapp.domain.usecase.GetProductsUseCase
import ru.shevrus.roomdbapp.domain.usecase.SyncProductsUseCase
import ru.shevrus.roomdbapp.presentation.util.UiErrorTranslator

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val syncProductsUseCase: SyncProductsUseCase,
    private val toggleFavoriteUseCase: ChangeFavoriteStatusUseCase,
    errorTranslator: UiErrorTranslator
) : BaseViewModel(errorTranslator) {

    private val PAGE_SIZE = 30
    private var currentSkip = 0
    private var isEndOfTheList = false

    private var totalServerCount: Int? = null

    val uiState: StateFlow<ProductsUiState> = combine(
        getProductsUseCase(),
        isLoading,
        errorMessage
    ) { products, loading, error ->
        ProductsUiState(products = products, isLoading = loading, errorMessage = error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductsUiState())

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLoading.value || isEndOfTheList) return

        safeLaunch(
            block = { syncProductsUseCase(limit = PAGE_SIZE, skip = currentSkip) },
            onSuccess = { total ->
                totalServerCount = total

                currentSkip += PAGE_SIZE

                val currentLoadedCount = uiState.value.products.size

                if (currentLoadedCount >= total) {
                    isEndOfTheList = true
                }
            }
        )
    }

    fun refreshAll() {
        currentSkip = 0
        isEndOfTheList = false
        totalServerCount = null
        loadNextPage()
    }

    fun toggleFavorite(productId: Long, isFavorite: Boolean) {
        safeLaunch(
            block = {
                Result.runCatching {
                    toggleFavoriteUseCase(productId, isFavorite)
                }
            }
        )
    }
}