package com.softgenix.abastock.features.sales.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // 🔥 IMPORTANTE

@Composable
fun CartItemCard(
    name: String,
    price: Double,
    quantity: Int,
    imageUrl: String? = null,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .background(Color(0xFFF1F3F5), RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Inventory, contentDescription = null, tint = Color.Gray)
                }
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("$$price c/u", fontSize = 12.sp, color = Color.Gray)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrease) {
                    Icon(if(quantity > 1) Icons.Default.Remove else Icons.Default.Delete, contentDescription = null)
                }
                Text("$quantity", fontWeight = FontWeight.Bold)
                IconButton(onClick = onIncrease) {
                    Icon(Icons.Default.AddCircle, contentDescription = null, tint = Color(0xFF1D3557))
                }
            }
            Text("$${price * quantity}", fontWeight = FontWeight.Bold)
        }
    }
}