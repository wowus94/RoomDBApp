package ru.shevrus.roomdbapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.usecase.ClearTableUseCase
import ru.shevrus.roomdbapp.domain.usecase.GetTableUseCase
import ru.shevrus.roomdbapp.domain.usecase.InsertTableUseCase
import ru.shevrus.roomdbapp.domain.usecase.SyncWithServerUseCase

class TodoViewModel(
    private val getTableUseCase: GetTableUseCase,
    private val insertTableUseCase: InsertTableUseCase,
    private val clearTableUseCase: ClearTableUseCase,
    private val syncWithServerUseCase: SyncWithServerUseCase
) : ViewModel() {

    val todosState: StateFlow<List<Todo>> = getTableUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            syncWithServerUseCase()
        }
    }

    fun insertTodo(text: String) {
        viewModelScope.launch {
            val newTodo = Todo(
                id = 0,
                text = text,
                isSynced = false,
                isDeletedLocally = false
            )
            insertTableUseCase(newTodo)

            syncWithServerUseCase()
        }
    }

    fun clearTable() {
        viewModelScope.launch {
            clearTableUseCase()
        }
    }
}