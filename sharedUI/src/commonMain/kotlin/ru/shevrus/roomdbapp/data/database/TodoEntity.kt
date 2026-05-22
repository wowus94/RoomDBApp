package ru.shevrus.roomdbapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String,
    val isSynced: Boolean = false,
    val isDeletedLocally: Boolean = false
)