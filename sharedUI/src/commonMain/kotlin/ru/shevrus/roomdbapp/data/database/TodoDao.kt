package ru.shevrus.roomdbapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(todoEntity: TodoEntity)

    @Query("SELECT * FROM TodoEntity")
    fun getAllAsFlow(): Flow<List<TodoEntity>>

    @Query("DELETE FROM TodoEntity")
    suspend fun clearTable()
}