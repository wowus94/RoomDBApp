package ru.shevrus.roomdbapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.shevrus.roomdbapp.presentation.ProductViewModel
import ru.shevrus.roomdbapp.presentation.ui_item.ProductCard

@Composable
fun ProductsScreen(
    productViewModel: ProductViewModel,
    paddingValues: PaddingValues
) {

    val state by productViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val pullToRefreshState = rememberPullToRefreshState()

    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false

            lastVisibleItem.index >= totalItems - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && state.products.isNotEmpty()) {
            productViewModel.loadNextPage()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            val result = snackbarHostState.showSnackbar(
                message = error,
                actionLabel = "Повторить",
                duration = SnackbarDuration.Long
            )

            if (result == SnackbarResult.ActionPerformed) {
                productViewModel.loadNextPage()
            } else {
                productViewModel.dismissError()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Каталог товаров") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        modifier = Modifier.fillMaxSize().padding(paddingValues),
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {

            when {
                state.products.isEmpty() && state.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.products.isEmpty() && state.errorMessage != null -> {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("❌", fontSize = 32.sp)
                        Spacer(Modifier.height(16.dp))
                        Text(state.errorMessage!!, textAlign = TextAlign.Center)
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { productViewModel.loadNextPage() }) {
                            Text("Повторить попытку")
                        }
                    }
                }

                state.products.isEmpty() -> {
                    Text(
                        text = "Нет доступных товаров",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isLoading,
                        state = pullToRefreshState,
                        onRefresh = { productViewModel.refreshAll() },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = state.products, key = { it.id }) { product ->
                                ProductCard(
                                    product = product,
                                    onFavoriteClick = {
                                        productViewModel.toggleFavorite(
                                            productId = product.id,
                                            !product.isFavorite
                                        )
                                    }
                                )
                            }

                            if (state.isLoading && state.products.isNotEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}