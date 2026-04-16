package com.softgenix.abastock.features.sales.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.core.shared.components.StyledInput
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.inventory.presentation.viewmodels.InventoryViewModel
import com.softgenix.abastock.features.sales.presentation.components.SelectableProductItem

@Composable
fun ProductSelectionScreen(
    onProductSelected: (InventoryItem) -> Unit,
    onBack: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            Header(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                Column(modifier = Modifier.padding(16.dp).padding(top = 20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBackIos, contentDescription = null, tint = Color.White)
                        }
                        Text("Seleccionar Productos", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))

                    StyledInput(
                        value = state.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = "Busca por nombre o marca...",
                        leadingIconRes = R.drawable.search_icon
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            else if (state.filteredItems.isEmpty()) {
                Text(
                    text = "No se encontraron productos",
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp),
                    textAlign = TextAlign.Center
                )
            }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.filteredItems) { item ->
                        SelectableProductItem(
                            name = item.productName,
                            brand = item.brandName,
                            price = item.salePrice,
                            stock = item.currentStock.toInt(),
                            imageUrl = item.imageUri,
                            onAdd = {
                                if (item.currentStock > 0) {
                                    onProductSelected(item)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}