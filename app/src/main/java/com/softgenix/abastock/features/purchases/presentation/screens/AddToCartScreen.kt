package com.softgenix.abastock.features.purchases.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.softgenix.abastock.core.ui.theme.AccentGold
import com.softgenix.abastock.core.ui.theme.GrayLight
import com.softgenix.abastock.core.ui.theme.InputBg
import com.softgenix.abastock.core.ui.theme.InputBorder
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.core.ui.theme.SuccessGreen
import com.softgenix.abastock.core.ui.theme.Surface
import com.softgenix.abastock.core.ui.theme.TextPrim
import com.softgenix.abastock.core.ui.theme.TextSec
import com.softgenix.abastock.features.inventory.presentation.components.ProductCard
import com.softgenix.abastock.features.purchases.presentation.viewmodels.AddToCartViewModel
import com.softgenix.abastock.features.purchases.presentation.viewmodels.SupplyViewModel

@Composable
fun AddToCartScreen(
    barcode: String,
    onAddedToCart: (String) -> Unit,
    viewModel: AddToCartViewModel = hiltViewModel(),
    supplyViewModel: SupplyViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val qtyNum = state.quantity.toDoubleOrNull() ?: 0.0
    val costNum = state.costPrice.toDoubleOrNull() ?: 0.0
    val saleNum = state.salePrice.toDoubleOrNull() ?: 0.0

    val subtotalInversion = qtyNum * costNum
    val gananciaPorUnidad = saleNum - costNum
    val margenPorcentaje = if (costNum > 0) (gananciaPorUnidad / costNum) * 100 else 0.0

    LaunchedEffect(barcode) {
        viewModel.loadProduct(barcode)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        ProductCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = GrayLight
                ) {
                    AsyncImage(
                        model = state.product?.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                    )
                }

                Spacer(Modifier.width(16.dp))

                Column {
                    Text(
                        text = state.product?.productName ?: "Cargando...",
                        fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrim
                    )
                    Text(
                        text = "${state.product?.brandName} · ${state.product?.unit}",
                        color = TextSec, fontSize = 13.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Surface(color = GrayLight, shape = RoundedCornerShape(8.dp)) {
                        Text(
                            text = barcode,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            fontSize = 11.sp, color = TextSec
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                "Cantidad comprada *",
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                color = TextPrim
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.quantity,
                onValueChange = viewModel::onQuantityChange,
                modifier = Modifier.fillMaxWidth(),
                suffix = { Text("unidades", color = TextSec) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = InputBg,
                    unfocusedBorderColor = InputBorder
                )
            )

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Precio compra *", fontWeight = FontWeight.Bold, color = TextPrim)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.costPrice,
                        onValueChange = viewModel::onCostChange,
                        prefix = { Text("$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text("Precio venta *", fontWeight = FontWeight.Bold, color = TextPrim)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.salePrice,
                        onValueChange = { viewModel.onSaleChange(it) },
                        prefix = { Text("$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }


        Spacer(Modifier.height(32.dp))
        val subtotal =
            (state.quantity.toDoubleOrNull() ?: 0.0) * (state.costPrice.toDoubleOrNull() ?: 0.0)

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE8F5E9),
            border = BorderStroke(1.dp, SuccessGreen)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Margen esperado", fontSize = 12.sp, color = SuccessGreen)
                    Text(
                        "${String.format("%.1f", margenPorcentaje)}%",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SuccessGreen
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Inversión total", fontSize = 12.sp, color = SuccessGreen)
                    Text(
                        "$${String.format("%.2f", subtotalInversion)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SuccessGreen
                    )
                }
            }
        }

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                try {
                    val qty = state.quantity.toIntOrNull() ?: 0
                    val cost = state.costPrice.toDoubleOrNull() ?: 0.0
                    val sale = state.salePrice.toDoubleOrNull() ?: 0.0
                    val product = state.product

                    if (product == null) {
                        return@Button
                    }

                    val item =
                        com.softgenix.abastock.features.purchases.domain.entities.SupplyTransactionItem(
                            itemId = java.util.UUID.randomUUID().toString(),
                            presentationId = product.presentationId,
                            inventoryId = product.id,
                            quantity = qty,
                            costPrice = cost,
                            salePrice = sale,
                            name = product.productName,
                            brand = product.brandName,
                            imageUrl = product.imageUrl
                        )

                    supplyViewModel.addToCart(item)

                    val finalStoreId = supplyViewModel.storeId.ifEmpty { "ID_DESCONOCIDO" }

                    onAddedToCart(finalStoreId)

                } catch (e: Exception) {
                    android.util.Log.e("crhaseo", "Error al agregar: ${e.message}")
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentGold)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Agregar al Carrito", fontWeight = FontWeight.Bold)
        }
    }
}