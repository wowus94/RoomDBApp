package ru.shevrus.roomdbapp.data.network

import kotlinx.coroutines.delay
import ru.shevrus.roomdbapp.domain.model.Todo

class FakeTodoApi {

    var shouldThrowError = false

    private val remoteStorage = mutableListOf<Todo>()

    suspend fun fetchTodos(): List<Todo> {
        if (shouldThrowError) throw Exception("No internet connection")

        delay(2000)
        return remoteStorage
    }

    suspend fun uploadTodo(todo: Todo) {
        if (shouldThrowError) throw Exception("Failed to upload data")

        delay(1000)
        remoteStorage.add(todo)
    }

    fun addRemoteTodos(list: List<Todo>) {
        remoteStorage.addAll(list)
    }
}