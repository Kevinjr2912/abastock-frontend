package com.softgenix.abastock.features.sales.presentation.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
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
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.core.shared.components.StyledInput
import com.softgenix.abastock.features.inventory.domain.entities.InventoryItem
import com.softgenix.abastock.features.sales.presentation.components.SelectableProductItem

@Composable
fun ProductSelectionScreen(
    onProductSelected: (InventoryItem) -> Unit,
    onBack: () -> Unit
) {
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
                        value = "",
                        onValueChange = {},
                        placeholder = "Busca por nombre o marca...",
                        leadingIconRes = R.drawable.search_icon
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(10) {
                SelectableProductItem(
                    name = "Sabritas Sal 45g",
                    brand = "Sabritas",
                    price = 18.0,
                    stock = 24,
                    onAdd = {  }
                )
            }
        }
    }
}