package com.softgenix.abastock.features.sales.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.features.sales.presentation.components.AddProductButton
import com.softgenix.abastock.features.sales.presentation.components.CartItemCard
import com.softgenix.abastock.features.sales.presentation.viewmodels.SalesViewModel

@Composable
fun SalesScreen(
    navController: NavHostController,
    onNavigateToSelection: () -> Unit,
    onNavigateToSuccess: (Double) -> Unit,
    viewModel: SalesViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onNavigateToSuccess(state.lastSaleTotal)
        }
    }

    Scaffold(
        topBar = {
            Header(modifier = Modifier.fillMaxWidth().height(100.dp)) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIos, contentDescription = null, tint = Color.White)
                    }
                    Text("Punto de Venta", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        },
        bottomBar = {
            Column(modifier = Modifier.background(Color.White).padding(16.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("${state.totalItems} artículos", color = Color.Gray)
                    Text("$${String.format("%.2f", state.totalAmount)}", color = Color.Gray)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("$${String.format("%.2f", state.totalAmount)}", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF1D3557))
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.processSale() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = state.cartItems.isNotEmpty() && !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(2.dp))
                    } else {
                        Text("Cobrar $${String.format("%.2f", state.totalAmount)}")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { AddProductButton(onClick = { onNavigateToSelection() }) }

            if (state.cartItems.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.LightGray, modifier = Modifier.padding(bottom = 16.dp))
                        Text("Tu carrito está vacío", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                        Text("Agrega productos escaneando \no buscando en tu inventario", color = Color.Gray, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            } else {
                items(state.cartItems) { item ->
                    CartItemCard(
                        name = item.name,
                        price = item.salePrice,
                        quantity = item.quantity,
                        imageUrl = item.imageUrl,
                        onIncrease = { viewModel.increaseQuantity(item.cartItemId) },
                        onDecrease = { viewModel.decreaseQuantity(item.cartItemId) }
                    )
                }
            }
        }
    }
}