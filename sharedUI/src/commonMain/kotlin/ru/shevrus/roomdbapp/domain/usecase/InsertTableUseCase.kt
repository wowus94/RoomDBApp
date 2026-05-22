package ru.shevrus.roomdbapp.domain.usecase

import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class InsertTableUseCase(private val todoRepository: TodoRepository) {
    suspend operator fun invoke(todo: Todo) = todoRepository.insert(todo)
}