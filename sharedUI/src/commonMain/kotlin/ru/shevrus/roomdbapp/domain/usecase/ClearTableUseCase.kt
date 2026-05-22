package ru.shevrus.roomdbapp.domain.usecase

import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class ClearTableUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke() = todoRepository.clearTable()
}