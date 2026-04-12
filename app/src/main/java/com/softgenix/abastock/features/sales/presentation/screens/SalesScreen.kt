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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.features.sales.presentation.components.AddProductButton
import com.softgenix.abastock.features.sales.presentation.components.CartItemCard

@Composable
fun SalesScreen(
    navController: NavHostController,
    onNavigateToSelection: () -> Unit,
    onNavigateToSuccess: (Double) -> Unit){
    Scaffold(
topBar = {
    Header(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBackIos, contentDescription = null, tint = Color.White)
            }
            Text(
                "Punto de Venta",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
},
bottomBar = {
    Column(modifier = Modifier
        .background(Color.White)
        .padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("4 artículos", color = Color.Gray)
            Text("$57.00", color = Color.Gray)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "$57.00",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color(0xFF1D3557)
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { onNavigateToSuccess(57.0) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Cobrar $57.00")
        }
    }
}
) {
    padding ->
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            AddProductButton(
                onClick = { onNavigateToSelection() }
            )
        }
        items(4) {
            CartItemCard("Coca-Cola 600ml", 15.0, 3, {}, {})
        }
    }
}
}