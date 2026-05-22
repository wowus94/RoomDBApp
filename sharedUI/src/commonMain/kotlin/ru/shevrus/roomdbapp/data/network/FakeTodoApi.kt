package ru.shevrus.roomdbapp.data.network

import kotlinx.coroutines.delay
import ru.shevrus.roomdbapp.domain.model.Todo

class FakeTodoApi {
    private val remoteStorage = mutableListOf<Todo>()

    suspend fun fetchTodos(): List<Todo> {
        delay(2000)
        return remoteStorage
    }

    suspend fun uploadTodo(todo: Todo) {
        delay(1000)
        remoteStorage.add(todo)
    }
}