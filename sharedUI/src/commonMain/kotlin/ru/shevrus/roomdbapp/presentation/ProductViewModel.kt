package ru.shevrus.roomdbapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.shevrus.roomdbapp.domain.usecase.GetProductsUseCase
import ru.shevrus.roomdbapp.domain.usecase.SyncProductsUseCase

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val syncProductsUseCase: SyncProductsUseCase
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ProductsUiState> = combine(
        getProductsUseCase(),
        _isLoading,
        _errorMessage
    ) { products, isLoading, error ->
        ProductsUiState(products = products, isLoading = isLoading, errorMessage = error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProductsUiState())

    init {
        loadAndSync()
    }

    fun loadAndSync() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = syncProductsUseCase()

            result.onFailure { exception ->
                val message = exception.message ?: ""
                if (message.contains("Unable to resolve host") || message.contains("No address")) {
                    _errorMessage.value = "Не удалось подключиться к серверу. Проверьте интернет-соединение."
                } else {
                    _errorMessage.value = "Произошла непредвиденная ошибка при обновлении каталога."
                }
            }

            _isLoading.value = false
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}