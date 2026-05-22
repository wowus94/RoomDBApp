package data

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ru.shevrus.roomdbapp.data.database.AppDatabase
import ru.shevrus.roomdbapp.data.database.TodoDao
import ru.shevrus.roomdbapp.data.network.FakeTodoApi
import ru.shevrus.roomdbapp.data.repository.TodoRepositoryImpl
import ru.shevrus.roomdbapp.domain.model.Todo
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class TodoRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var todoDao: TodoDao
    private lateinit var fakeApi: FakeTodoApi
    private lateinit var repository: TodoRepositoryImpl

    @BeforeTest
    fun setup() {
        database = Room.inMemoryDatabaseBuilder<AppDatabase>()
            .setDriver(BundledSQLiteDriver())
            .build()

        todoDao = database.getDao()
        fakeApi = FakeTodoApi()

        repository = TodoRepositoryImpl(api = fakeApi, todoDao = todoDao)
    }

    @AfterTest
    fun tearDown() {
        database.close()
    }

    @Test
    fun `syncWithServer success should upload unsynced and download remote updates`() = runTest {
        val localTodo = Todo(id = 1, text = "Local Offline Task", isSynced = false, isDeletedLocally = false)
        repository.insert(localTodo)

        val serverTodo = Todo(id = 2, text = "Server Task", isSynced = true, isDeletedLocally = false)
        fakeApi.addRemoteTodos(listOf(serverTodo))

        fakeApi.shouldThrowError = false
        repository.syncWithServer()

        val currentLocalTodos = repository.getAll().first()

        assertEquals(2, currentLocalTodos.size)

        val updatedLocal = currentLocalTodos.find { it.id == 1 }
        assertNotNull(updatedLocal)
        assertTrue(updatedLocal.isSynced)

        val downloadedTodo = currentLocalTodos.find { it.id == 2 }
        assertNotNull(downloadedTodo)
        assertTrue(downloadedTodo.isSynced)
    }

    @Test
    fun `syncWithServer network failure should not crash app and keep data locally`() = runTest {
        val localTodo = Todo(id = 1, text = "Important Local Note", isSynced = false, isDeletedLocally = false)
        repository.insert(localTodo)

        fakeApi.shouldThrowError = true

        try {
            repository.syncWithServer()
        } catch (e: Exception) {
            fail("Репозиторий не должен выбрасывать ошибку наружу, он должен гасить её в catch блоке")
        }

        val currentLocalTodos = repository.getAll().first()
        assertEquals(1, currentLocalTodos.size)

        val todoAfterFailedSync = currentLocalTodos.first()
        assertFalse(todoAfterFailedSync.isSynced)
    }
}