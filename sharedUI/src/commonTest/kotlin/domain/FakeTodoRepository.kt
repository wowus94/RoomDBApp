package domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class FakeTodoRepository : TodoRepository {
    private val todos = mutableListOf<Todo>()
    var syncCalled = false

    override fun getAll(): Flow<List<Todo>> = flow { emit(todos) }

    override suspend fun insert(todo: Todo) {
        todos.add(todo)
    }

    override suspend fun syncWithServer() {
        syncCalled = true
    }

    override suspend fun clearTable() {
        todos.clear()
    }
}