package com.softgenix.abastock.features.purchases.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.softgenix.abastock.R
import com.softgenix.abastock.core.hardware.domain.ScannerManager
import com.softgenix.abastock.core.ui.theme.AccentGold
import com.softgenix.abastock.core.ui.theme.BlueStrong
import com.softgenix.abastock.core.ui.theme.InputBg
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.core.ui.theme.Surface
import com.softgenix.abastock.core.ui.theme.TextPrim
import com.softgenix.abastock.core.ui.theme.TextSec
import com.softgenix.abastock.core.ui.theme.WhitePure
import com.softgenix.abastock.features.purchases.domain.entities.PurchaseItem
import com.softgenix.abastock.features.purchases.presentation.components.CameraPreview
import com.softgenix.abastock.features.purchases.presentation.viewmodels.PurchaseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PurchaseScannerScreen(
    viewModel: PurchaseViewModel = hiltViewModel(),
    scannerManager: ScannerManager,
    onNavigateToCreate: (String) -> Unit,
    onNavigateToExisting: (String) -> Unit

) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val scope = rememberCoroutineScope()
    val haptic = androidx.compose.ui.platform.LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Surface)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(BlueStrong)
        ) {
            if (cameraPermissionState.status.isGranted) {
                CameraPreview(
                    scannerManager = scannerManager,
                    onBarcodeDetected = { code ->
                        viewModel.onBarcodeScanned(
                            barcode = code,
                            onExists = { product -> onNavigateToExisting(product.barcode) },
                            onNotFound = { barcode -> onNavigateToCreate(barcode) }
                        )
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_scanner_frame),
                    contentDescription = null,
                    tint = AccentGold,
                    modifier = Modifier.size(120.dp)
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (cameraPermissionState.status.isGranted)
                        "Enfoca el código de barras" else "Sin permiso de cámara",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (cameraPermissionState.status.isGranted) {
                    } else {
                        cameraPermissionState.launchPermissionRequest()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyMid) ){
                Icon(Icons.Default.PhotoCamera, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Escanear Ahora", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = WhitePure,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color(0xFFFFF1E0)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = AccentGold)
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            "Registra múltiples productos",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = TextPrim
                        )
                        Text(
                            "Escanea todos los productos de tu compra uno por uno",
                            fontSize = 12.sp,
                            color = TextSec
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "¿No puedes escanear?",
                fontSize = 13.sp,
                color = TextSec,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Escribe el código manualmente", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                trailingIcon = {
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(40.dp)
                            .background(NavyMid, RoundedCornerShape(10.dp))

                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = InputBg,
                    unfocusedContainerColor = InputBg,
                    focusedBorderColor = NavyMid,
                    unfocusedBorderColor = Color.Transparent,
                )
            )
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
                .clickable(enabled = false) { },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AccentGold)
        }
    }

}