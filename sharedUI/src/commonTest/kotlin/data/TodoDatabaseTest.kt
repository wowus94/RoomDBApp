package data

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ru.shevrus.roomdbapp.data.database.AppDatabase
import ru.shevrus.roomdbapp.data.database.TodoDao
import ru.shevrus.roomdbapp.data.database.TodoEntity
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TodoDatabaseTest {
    private lateinit var database: AppDatabase
    private lateinit var todoDao: TodoDao

    @BeforeTest
    fun createDb() {
        val builder = Room.inMemoryDatabaseBuilder<AppDatabase>()

        database = builder
            .setDriver(BundledSQLiteDriver())
            .build()

        todoDao = database.getDao()
    }

    @AfterTest
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndReadTodo() = runTest {

        val entity = TodoEntity(id = 1, text = "Room KMP Test", isSynced = false)

        todoDao.insert(entity)

        val result = todoDao.getAllAsFlow().first()
        assertEquals(1, result.size)
        assertEquals("Room KMP Test", result.first().text)
    }

    @Test
    fun clearTable_shouldDeleteAllRecords() = runTest {

        todoDao.insert(TodoEntity(id = 1, text = "Task 1"))
        todoDao.insert(TodoEntity(id = 2, text = "Task 2"))

        todoDao.clearTable()

        val result = todoDao.getAllAsFlow().first()
        assertEquals(0, result.size)
    }
}