package ru.shevrus.roomdbapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.shevrus.roomdbapp.presentation.util.UiErrorTranslator

abstract class BaseViewModel(
    private val errorTranslator: UiErrorTranslator
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    protected fun <T> safeLaunch(
        block: suspend () -> Result<T>,
        onSuccess: (T) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = block()

            result.onSuccess { data ->
                onSuccess(data)
            }.onFailure { exception ->

                _errorMessage.value = errorTranslator.translate(exception)
            }

            _isLoading.value = false
        }
    }

    fun dismissError() {
        _errorMessage.value = null
    }
}