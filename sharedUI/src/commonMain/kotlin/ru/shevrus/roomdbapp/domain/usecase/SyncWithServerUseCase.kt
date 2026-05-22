package ru.shevrus.roomdbapp.domain.usecase

import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class SyncWithServerUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke() {
        repository.syncWithServer()
    }
}