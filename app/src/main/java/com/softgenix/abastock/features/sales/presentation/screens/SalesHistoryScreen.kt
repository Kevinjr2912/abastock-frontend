package com.softgenix.abastock.features.sales.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Download
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
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.core.ui.theme.Surface

@Composable
fun SalesHistoryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToNewSale: () -> Unit // Para el botón flotante o menú de abajo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyMid) // El fondo azul marino del diseño
    ) {
        // 1. Header (Ventas + Botón Descargar)
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
            IconButton(
                onClick = { /* TODO: Descargar reporte */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
            ) {
                Icon(Icons.Default.Download, contentDescription = "Descargar", tint = Color.White)
            }
        }

        // 2. Tabs (Hoy, Semana, Mes) - Versión Mock
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White.copy(alpha = 0.1f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // "Hoy" Seleccionado
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
            // Los otros inactivos
            Box(modifier = Modifier.weight(1f).padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                Text("Semana", color = Color.White)
            }
            Box(modifier = Modifier.weight(1f).padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                Text("Mes", color = Color.White)
            }
        }

        // 3. Total del día
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Total del día", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$404.00", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 36.sp)
                Spacer(modifier = Modifier.width(12.dp))
                // Pill verde de porcentaje
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8F5E9)) // Verde clarito
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("↗ +18%", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // 4. White Sheet Background para la lista
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
            color = Color(0xFFF8F9FA) // Un gris muy clarito casi blanco
        ) {
            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Encabezado de la lista
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("5 transacciones", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        OutlinedButton(
                            onClick = { /* TODO: Filtros */ },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp)
                        ) {
                            Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Filtrar", color = Color.Black)
                        }
                    }
                }

                // Lista de transacciones Mock
                items(5) { index ->
                    TransactionItemMock(index = index)
                }
            }
        }
    }
}

// Componente para cada Tarjeta de Venta
@Composable
fun TransactionItemMock(index: Int) {
    var expanded by remember { mutableStateOf(index == 0) } // El primero empieza expandido para probar

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Fila principal siempre visible
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
                    Text("Venta #000${index + 1}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("08:12 a.m. • 3 artículos", color = Color.Gray, fontSize = 12.sp)
                }
                Text("$44.00", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = NavyMid)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }

            // Detalle expandible
            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = Color(0xFFF0F4F8))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Detalle de la venta", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    // Producto 1
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).background(Color.LightGray, RoundedCornerShape(8.dp))) // Placeholder imagen
                        Spacer(Modifier.width(8.dp))
                        Text("Coca-Cola 600ml", modifier = Modifier.weight(1f), fontSize = 14.sp)
                        Text("×2", color = Color.Gray, fontSize = 14.sp)
                        Spacer(Modifier.width(16.dp))
                        Text("$30.00", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    // Producto 2
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).background(Color.LightGray, RoundedCornerShape(8.dp))) // Placeholder imagen
                        Spacer(Modifier.width(8.dp))
                        Text("Sabritas Original 45g", modifier = Modifier.weight(1f), fontSize = 14.sp)
                        Text("×1", color = Color.Gray, fontSize = 14.sp)
                        Spacer(Modifier.width(16.dp))
                        Text("$14.00", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF0F4F8))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Total", fontWeight = FontWeight.Bold)
                        Text("$44.00", fontWeight = FontWeight.ExtraBold, color = NavyMid)
                    }
                }
            }
        }
    }
}