package com.softgenix.abastock.features.purchases.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.softgenix.abastock.core.ui.theme.AccentGold
import com.softgenix.abastock.core.ui.theme.ErrorRed
import com.softgenix.abastock.core.ui.theme.GrayLight
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.core.ui.theme.SuccessGreen
import com.softgenix.abastock.core.ui.theme.Surface
import com.softgenix.abastock.core.ui.theme.TextPrim
import com.softgenix.abastock.core.ui.theme.TextSec
import com.softgenix.abastock.features.inventory.presentation.components.ProductCard
import com.softgenix.abastock.features.purchases.presentation.viewmodels.SupplyViewModel

@Composable
fun PurchaseSummaryScreen(
    onFinish: () -> Unit,
    onScanAnother: () -> Unit,
    storeId: String,
    viewModel: SupplyViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    if (state.isLoading) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = AccentGold)
                    Spacer(Modifier.height(8.dp))
                    Text("Espera...", fontSize = 12.sp)
                }
            }
        }
    }


    LaunchedEffect(storeId) {
        viewModel.setStoreId(storeId)
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) { onFinish() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Productos en la compra (${state.cartItems.size})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrim,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }

            items(state.cartItems) { item ->
                ProductCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(65.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = GrayLight
                        ) {
                            AsyncImage(
                                model = item.imageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(item.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(item.brand, color = TextSec, fontSize = 12.sp)

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column {
                                    Text("Cantidad", fontSize = 10.sp, color = TextSec)
                                    Text(
                                        "${item.quantity} uds",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column {
                                    Text("P. Compra", fontSize = 10.sp, color = TextSec)
                                    Text(
                                        "$${String.format("%.2f", item.costPrice)}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {

                            Spacer(Modifier.height(16.dp))
                            Text("Subtotal", fontSize = 10.sp, color = TextSec)
                            Text(
                                text = "$${String.format("%.2f", item.quantity * item.costPrice)}",
                                color = AccentGold,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = onScanAnother,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, NavyMid)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = NavyMid)
                Spacer(Modifier.width(8.dp))
                Text("Escanear Otro Producto", color = NavyMid, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    viewModel.finishPurchase(storeId)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SuccessGreen,
                    disabledContainerColor = GrayLight
                ),
                enabled = !state.isLoading && state.cartItems.isNotEmpty()
            ) {
                if (state.isLoading) {
                    Text("Procesando...")
                } else {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Finalizar Compra", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}