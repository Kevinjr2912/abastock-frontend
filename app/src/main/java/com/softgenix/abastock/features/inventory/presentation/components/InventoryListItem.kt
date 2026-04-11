package com.softgenix.abastock.features.inventory.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InventoryListItem(
    name: String,
    brand: String,
    price: Double,
    stock: Int,
    category: String,
    imageUrl: String? = null,
    maxStock: Int = 50
) {
    //hay se lo pongo dps en el viewmodel
    val isLowStock = stock < 5
    val statusColor = if (isLowStock) Color(0xFFF44336) else Color(0xFF4CAF50)
    val statusText = if (isLowStock) "$stock · ¡Agotándose!" else "$stock · En stock"

    ProductCard(
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier.size(75.dp)
                    .padding(end = 10.dp),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF8F9FB)
            ) {
                coil.compose.AsyncImage(
                    model = imageUrl,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                    error = androidx.compose.ui.graphics.painter.ColorPainter(Color(0xFFF8F9FB)),
                    fallback = androidx.compose.ui.graphics.painter.ColorPainter(Color(0xFFF8F9FB))
                )


                if (imageUrl == null) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = Color.LightGray,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    color = Color(0xFF1A1C1E)
                )
                Text(
                    text = "$brand · $category",
                    color = Color(0xFF8E97A8),
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = stock.toFloat() / maxStock,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(5.dp)
                        .clip(CircleShape),
                    color = statusColor,
                    trackColor = statusColor.copy(alpha = 0.1f)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", price)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color.LightGray,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}