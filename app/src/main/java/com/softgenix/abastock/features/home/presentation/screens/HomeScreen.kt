package com.softgenix.abastock.features.home.presentation.screens

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.AbastockBottomBar
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.core.shared.components.HeaderBubble
import com.softgenix.abastock.core.shared.entities.Bubble
import com.softgenix.abastock.features.home.presentation.components.QuickActionItem
import com.softgenix.abastock.features.home.presentation.components.RecentSaleItem
import com.softgenix.abastock.features.home.presentation.components.SummarySaleCard
import com.softgenix.abastock.features.home.presentation.components.TopProductCard

@Composable
fun HomeScreen(
    onNavigateToPay: () -> Unit,
    onNavigateToAddProducts: () -> Unit,
    onNavigateToInventory: () -> Unit,
    onNavigateToSales: () -> Unit,
    navController: NavController
) {

    Scaffold(

        bottomBar = {
            AbastockBottomBar(navController)
        },
        topBar = {
            Header(modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)) {
                HeaderBubble(
                    Bubble(130, 30, 20),
                    Bubble(180, -40, -40)
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "ABASTOCK",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 22.sp,
                        letterSpacing = 3.sp
                    )
                    Text(
                        text = "Abarrotes María ", // va a ser el name de la tienda
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        SummarySaleCard(
                            title = "Ventas de hoy",
                            amount = "$404.00",
                            subtitle = "+18% vs ayer",
                            containerColor = com.softgenix.abastock.core.ui.theme.BlueLight,
                            modifier = Modifier.weight(1f)
                        )

                        SummarySaleCard(
                            title = "Esta semana",
                            amount = "$10,196.00",
                            subtitle = "7 días registrados",
                            containerColor = com.softgenix.abastock.core.ui.theme.AccentGold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

            }
        },
        containerColor = Color(0xFFF7F8FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {

            item {
                Text(
                    text = "Acciones rápidas",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionItem(
                        title = "Cobrar",
                        iconRes = R.drawable.ic_scanner,
                        containerColor = Color(0xFF1E3A5F),
                        contentColor = Color(0xFFECFDF5),
                        onClick = onNavigateToPay
                    )

                    QuickActionItem(
                        title = "Agregar",
                        iconRes = R.drawable.ic_agregar,
                        containerColor = Color(0xFF16A34A),
                        contentColor = Color(0xFFECFDF5),
                        onClick = onNavigateToAddProducts
                        )

                    QuickActionItem(
                        title = "Ventas",
                        iconRes = R.drawable.ic_ventas,
                        containerColor = Color(0xFFD97706),
                        contentColor = Color(0xFFECFDF5),
                        onClick= onNavigateToSales
                    )

                    QuickActionItem(
                        title = "Stock",
                        iconRes = R.drawable.ic_inventory,
                        containerColor = Color(0xFF7C3AED),
                        contentColor = Color(0xFFECFDF5),
                        onClick = onNavigateToInventory
                    )
                }
                Spacer(Modifier.height(28.dp))
            }

            item {
                Text(
                    "Más vendidos",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(5, key = { it }) {
                        TopProductCard("Coca-Cola 600ml", 15.0, 312)
                    }
                }
            }

            item {
                Text(
                    text = "Ventas recientes",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
                )
            }

            items(5, key = { index -> "recent_sale_$index" }) { index ->
                RecentSaleItem(
                    name = "Coca-Cola 600ml",
                    time = "10:25 AM",
                    amount = 15.00
                )
            }
        }
    }
}


