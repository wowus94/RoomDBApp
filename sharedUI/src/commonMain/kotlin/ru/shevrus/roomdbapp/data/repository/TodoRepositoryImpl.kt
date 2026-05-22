package ru.shevrus.roomdbapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.shevrus.roomdbapp.data.database.TodoDao
import ru.shevrus.roomdbapp.data.mapper.toDomain
import ru.shevrus.roomdbapp.data.mapper.toEntity
import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.repository.TodoRepository

class TodoRepositoryImpl(private val todoDao: TodoDao) : TodoRepository {

    override fun getAll(): Flow<List<Todo>> {
        return todoDao.getAllAsFlow().map { entityList ->
            entityList.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun insert(todo: Todo) {
        todoDao.insert(todo.toEntity())
    }

    override suspend fun clearTable() {
        todoDao.clearTable()
    }

}