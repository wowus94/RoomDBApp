package domain

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import ru.shevrus.roomdbapp.domain.model.Todo
import ru.shevrus.roomdbapp.domain.usecase.GetTableUseCase
import ru.shevrus.roomdbapp.domain.usecase.InsertTableUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class UseCaseTest {

    @Test
    fun `insert todo should add item to repository`() = runTest {

        val fakeRepository = FakeTodoRepository()
        val insertUseCase = InsertTableUseCase(fakeRepository)
        val getUseCase = GetTableUseCase(fakeRepository)
        val newTodo =
            Todo(id = 1, text = "Practice testing", isSynced = false, isDeletedLocally = false)

        insertUseCase(newTodo)

        val currentList = getUseCase().first()
        assertEquals(1, currentList.size)
        assertEquals("Practice testing", currentList.first().text)
    }
}