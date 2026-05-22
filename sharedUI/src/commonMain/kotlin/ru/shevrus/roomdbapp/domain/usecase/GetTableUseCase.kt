package ru.shevrus.roomdbapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class GetTableUseCase(private val todoRepository: TodoRepository) {
    operator fun invoke(): Flow<List<Todo>> {
        return todoRepository.getAll()
    }
}