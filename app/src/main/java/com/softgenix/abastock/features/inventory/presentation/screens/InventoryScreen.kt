package com.softgenix.abastock.features.inventory.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.AbastockBottomBar
import com.softgenix.abastock.core.shared.components.BackButton
import com.softgenix.abastock.core.shared.components.FilterCategory
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.core.shared.components.HeaderBubble
import com.softgenix.abastock.core.shared.components.StyledInput
import com.softgenix.abastock.core.shared.entities.Bubble
import com.softgenix.abastock.features.home.presentation.components.SummarySaleCard
import com.softgenix.abastock.features.inventory.presentation.components.InventoryListItem
import com.softgenix.abastock.features.inventory.presentation.viewmodels.InventoryViewModel

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InventoryScreen(
    navController: NavHostController,
    onAddPurchaseClick: () -> Unit,
    viewModel: InventoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    //para pedir permiso del micro
    val permissionState = rememberPermissionState(
        permission = android.Manifest.permission.RECORD_AUDIO
    )

    Scaffold(
        topBar = {
            Header(modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)) {
                HeaderBubble(
                    Bubble(130, 30, 20),
                    Bubble(180, -40, -40)
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 18.dp)
                        .padding(top = 45.dp)
                ) {
                    BackButton(
                        nameAction = "Inventario",
                    )
                    Spacer(Modifier.height(12.dp))

                    StyledInput(
                        value = state.searchQuery,
                        onValueChange = viewModel::onSearchQueryChange,
                        placeholder = "Nombre, marca o código...",
                        leadingIconRes = R.drawable.search_icon
                    )
                }
            }
        },

        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(start=30.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FloatingActionButton(
                    onClick= {
                        if (permissionState.status.isGranted) {
                            viewModel.startVoiceSearch()
                        } else {
                            permissionState.launchPermissionRequest()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Mic, contentDescription = "Voz", tint = Color.White)
                }
                FloatingActionButton(
                    onClick = onAddPurchaseClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Comprar", tint = Color.White)
                }
            }
        },
        bottomBar = { AbastockBottomBar(navController) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(10.dp)
        ) {

            if (state.isLoading) {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(8.dp))
                        Text("Cargando inventario...", color = Color.Gray)
                    }
                }
            }

            item {
                val categorias =
                    listOf("Todos", "Bebidas", "Botanas", "Lácteos", "Panadería", "Enlatados")
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categorias) { cat ->
                        FilterCategory(
                            text = cat,
                            isSelected = cat == state.selectedCategory,
                            onSelected = { viewModel.onCategorySelected(cat) }
                        )
                    }
                }
            }


            if (!state.isLoading && state.filteredItems.isEmpty()) {
                item {
                    Text(
                        "No se encontraron productos",
                        modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }

            items(state.filteredItems) { item ->
                InventoryListItem(
                    name = item.productName,
                    brand = item.brandName,
                    price = item.salePrice,
                    stock = item.currentStock.toInt(),
                    category = item.categoryName,
                    imageUrl = item.imageUri
                )
            }
        }
    }
}

