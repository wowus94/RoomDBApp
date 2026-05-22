package ru.shevrus.roomdbapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shevrus.roomdbapp.data.database.TodoDao
import ru.shevrus.roomdbapp.data.mapper.toDomain
import ru.shevrus.roomdbapp.data.mapper.toEntity
import ru.shevrus.roomdbapp.data.network.FakeTodoApi
import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class TodoRepositoryImpl(
    private val api: FakeTodoApi,
    private val todoDao: TodoDao
) : TodoRepository {

    override fun getAll(): Flow<List<Todo>> {
        return todoDao.getAllAsFlow().map { entityList ->
            entityList
                .filter { !it.isDeletedLocally }
                .map { entity -> entity.toDomain() }
        }
    }

    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo.toEntity().copy(isSynced = false))
    }

    override suspend fun syncWithServer() {
        try {
            val unsynced = todoDao.getUnsyncedTodos()
            unsynced.forEach { localEntity ->
                api.uploadTodo(localEntity.toDomain())
                todoDao.insert(localEntity.copy(isSynced = true))
            }

            val remoteTodos = api.fetchTodos()

            val remoteEntities = remoteTodos.map { it.toEntity().copy(isSynced = true) }
            todoDao.updateData(remoteEntities)

        } catch (e: Exception) {
            println("Offline-First Sync Error: ${e.message}")
        }
    }

    override suspend fun clearTable() {
        todoDao.clearTable()
    }
}