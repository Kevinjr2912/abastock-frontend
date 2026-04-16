package com.softgenix.abastock.features.sales.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.features.sales.domain.entities.SaleHistoryRecord
import com.softgenix.abastock.features.sales.presentation.viewmodels.SalesHistoryViewModel

@Composable
fun SalesHistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNewSale: () -> Unit,
    viewModel: SalesHistoryViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyMid)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBackIos, contentDescription = "Atrás", tint = Color.White)
                }
                Text("Ventas", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Hoy", color = NavyMid, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.weight(1f).padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                Text("Semana", color = Color.White)
            }
            Box(modifier = Modifier.weight(1f).padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                Text("Mes", color = Color.White)
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text("Total del día", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                val totalRevenue = state.salesData?.totalRevenue ?: 0.0
                Text("$${String.format("%.2f", totalRevenue)}", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            color = Color(0xFFF8F9FA)
        ) {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NavyMid)
                }
            } else if (state.errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(state.errorMessage ?: "Error desconocido", color = Color.Red)
                }
            } else if (state.salesData?.salesHistory?.isEmpty() == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay ventas registradas hoy", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val transactionCount = state.salesData?.transactionCount ?: 0
                            Text("$transactionCount transacciones", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }

                    val salesHistory = state.salesData?.salesHistory ?: emptyList()
                    items(salesHistory.size) { index ->
                        TransactionItemReal(record = salesHistory[index], index = index)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItemReal(record: SaleHistoryRecord, index: Int) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF0F4F8)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = NavyMid)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    val shortId = record.saleId.take(6).uppercase()
                    Text("Venta #$shortId", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${record.time} • ${record.totalItems} artículos", color = Color.Gray, fontSize = 12.sp)
                }
                Text("$${String.format("%.2f", record.totalAmount)}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyMid)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFF0F4F8))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Detalle de la venta", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    record.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.productName, modifier = Modifier.weight(1f), fontSize = 14.sp)
                            Text("×${item.quantity}", color = Color.Gray, fontSize = 14.sp)
                            Spacer(Modifier.width(16.dp))
                            Text("$${String.format("%.2f", item.subtotal)}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF0F4F8))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("$${String.format("%.2f", record.totalAmount)}", fontWeight = FontWeight.ExtraBold, color = NavyMid)
                    }
                }
            }
        }
    }
}