package ru.shevrus.roomdbapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity)

    @Query("SELECT * FROM TodoEntity")
    fun getAllAsFlow(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM TodoEntity WHERE isSynced = 0")
    suspend fun getUnsyncedTodos(): List<TodoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateData(todos: List<TodoEntity>)

    @Query("DELETE FROM TodoEntity")
    suspend fun clearTable()
}