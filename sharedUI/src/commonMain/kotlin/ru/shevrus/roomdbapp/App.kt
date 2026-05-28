package ru.shevrus.roomdbapp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import roomdbapp.sharedui.generated.resources.Res
import roomdbapp.sharedui.generated.resources.ic_list
import roomdbapp.sharedui.generated.resources.ic_not_favorite_star
import ru.shevrus.roomdbapp.navigation.Screen
import ru.shevrus.roomdbapp.presentation.ProductViewModel
import ru.shevrus.roomdbapp.presentation.screens.FavoritesScreen
import ru.shevrus.roomdbapp.presentation.screens.ProductsScreen
import ru.shevrus.roomdbapp.theme.AppTheme

@Preview
@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {

    val productViewModel: ProductViewModel = koinInject()

    var currentScreen by remember { mutableStateOf(Screen.Products) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentScreen == Screen.Products,
                    onClick = { currentScreen = Screen.Products },
                    icon = { Icon(imageVector = vectorResource(Res.drawable.ic_list), null) },
                    label = { Text("Каталог") }
                )
                NavigationBarItem(
                    selected = currentScreen == Screen.Favorites,
                    onClick = { currentScreen = Screen.Favorites },
                    icon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.ic_not_favorite_star),
                            null
                        )
                    },
                    label = { Text("Избранное") }
                )
            }
        }
    ) { paddingValues ->
        when (currentScreen) {
            Screen.Products -> ProductsScreen(
                productViewModel = productViewModel,
                paddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
            )

            Screen.Favorites -> FavoritesScreen(
                paddingValues = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
                productViewModel = productViewModel
            )
        }
    }
}