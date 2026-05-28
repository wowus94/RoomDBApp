package ru.shevrus.roomdbapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shevrus.roomdbapp.presentation.ProductViewModel
import ru.shevrus.roomdbapp.presentation.ui_item.ProductCard

@Composable
fun FavoritesScreen(
    paddingValues: PaddingValues,
    productViewModel: ProductViewModel
) {

    val state by productViewModel.uiState.collectAsStateWithLifecycle()
    val favoriteProducts = remember(state.products) {
        state.products.filter { it.isFavorite }
    }

    Scaffold(
        modifier = Modifier.padding(paddingValues),
        topBar = { CenterAlignedTopAppBar(title = { Text("Избранное") }) }
    ) { innerPadding ->
        if (favoriteProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Список избранного пуст")
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = favoriteProducts, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        onFavoriteClick = {
                            productViewModel.toggleFavorite(
                                productId = product.id,
                                isFavorite = !product.isFavorite
                            )
                        }
                    )
                }
            }
        }
    }
}