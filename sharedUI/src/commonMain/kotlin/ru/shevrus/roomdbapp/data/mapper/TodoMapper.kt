package ru.shevrus.roomdbapp.data.mapper

import ru.shevrus.roomdbapp.data.database.TodoEntity
import ru.shevrus.roomdbapp.domain.model.Todo

fun TodoEntity.toDomain(): Todo {
    return Todo(id, text, isSynced, isDeletedLocally)
}

fun Todo.toEntity(): TodoEntity {
    return TodoEntity(id, text, isSynced, isDeletedLocally)
}

