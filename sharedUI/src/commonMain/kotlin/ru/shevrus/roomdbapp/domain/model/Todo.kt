package ru.shevrus.roomdbapp.domain.model

data class Todo(
    val id: Int,
    val text: String,
    val isSynced: Boolean,
    val isDeletedLocally: Boolean
)