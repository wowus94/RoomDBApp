package ru.shevrus.roomdbapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.koinInject
import ru.shevrus.roomdbapp.presentation.TodoViewModel
import ru.shevrus.roomdbapp.theme.AppTheme
import kotlin.time.Clock

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {

    val todoViewModel: TodoViewModel = koinInject()

    val todos by todoViewModel.todosState.collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            todoViewModel.insertTodo(
                text = "Test ${Clock.System.now()}"
            )
        }) {
            Text("Insert")
        }

        Button(onClick = {
            todoViewModel.clearTable()
        }) {
            Text("Delete")
        }

        Spacer(Modifier.height(24.dp))

        Text("Todos:")

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = todos, key = { it.id }) { todo ->
                Text(
                    text = "${todo.id} — ${todo.text}",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

    }
}
