package ru.shevrus.roomdbapp.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.shevrus.roomdbapp.data.database.TodoEntity
import ru.shevrus.roomdbapp.domain.model.Todo

interface TodoRepository {

    fun getAll(): Flow<List<Todo>>

    suspend fun insert(todo: Todo)

    suspend fun clearTable()
}