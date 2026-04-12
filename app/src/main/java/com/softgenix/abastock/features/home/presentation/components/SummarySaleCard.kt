package com.softgenix.abastock.features.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummarySaleCard(
    title: String,
    amount: String,
    subtitle: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(24.dp),
        color = containerColor,
        shadowElevation = 4.dp,

    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = if (containerColor == com.softgenix.abastock.core.ui.theme.AccentGold)
                    com.softgenix.abastock.core.ui.theme.BlueStrong
                else Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp
            )
            Text(
                text = amount,
                color = if (containerColor == com.softgenix.abastock.core.ui.theme.AccentGold)
                    com.softgenix.abastock.core.ui.theme.BlueStrong
                else Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = subtitle,
                    color = if (containerColor == com.softgenix.abastock.core.ui.theme.AccentGold)
                        com.softgenix.abastock.core.ui.theme.BlueStrong.copy(alpha = 0.7f)
                    else Color(0xFF4ADE80),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}