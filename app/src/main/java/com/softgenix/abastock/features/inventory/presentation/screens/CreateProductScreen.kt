package com.softgenix.abastock.features.inventory.presentation.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.InputLabel
import com.softgenix.abastock.core.shared.components.StyledInput
import com.softgenix.abastock.core.ui.theme.TextSec
import com.softgenix.abastock.features.inventory.presentation.components.BrandDropdown
import com.softgenix.abastock.features.inventory.presentation.components.CategoryDropdown
import com.softgenix.abastock.features.inventory.presentation.viewmodels.CreateProductViewModel

@Composable
fun CreateProductScreen(
    barcode: String,
    onProductCreated: (String) -> Unit,
    viewModel: CreateProductViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val colorScheme = MaterialTheme.colorScheme

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onProductCreated(barcode)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImageSelected(it) }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
                .background(colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        // img y seccion
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    modifier = Modifier
                        .size(130.dp)
                        .clickable { galleryLauncher.launch("image/*") },
                    shape = RoundedCornerShape(24.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    if (state.selectedImageUri != null) {
                        AsyncImage(
                            model = state.selectedImageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = colorScheme.onBackground.copy(alpha = 0.2f),
                            modifier = Modifier.padding(35.dp)
                        )

                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Código del producto: $barcode",
                color = TextSec,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // info
        Surface(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color.White
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Información del producto",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))

                InputLabel("Nombre")
                StyledInput(
                    value = state.name,
                    placeholder = "name",
                    onValueChange = viewModel::onNameChange,
                    leadingIconRes = R.drawable.name_icon,
                )

                Spacer(Modifier.height(12.dp))

                InputLabel("Marca / Fabricante")
                BrandDropdown(
                    selectedBrand = state.selectedBrand,
                    brands = state.brands,
                    onBrandSelected = viewModel::onBrandSelect
                )


                Spacer(Modifier.height(12.dp))

                InputLabel("Presentación")
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        StyledInput(
                            value = state.value, // "1"
                            placeholder = "Cant.",
                            onValueChange = { viewModel.onValueChange(it) },
                            leadingIconRes = R.drawable.presentation_icon,

                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        StyledInput(
                            value = state.unit,
                            placeholder = "Unidad",
                            onValueChange = { viewModel.onUnitChange(it) },
                            leadingIconRes = R.drawable.presentation_icon,
                        )
                    }
                }


                InputLabel("Categoría")
                CategoryDropdown(
                    selectedCategory = state.selectedCategory,
                    categories = state.categories,
                    onCategorySelected = viewModel::onCategorySelect
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {viewModel.saveProduct(barcode)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .height(58.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorScheme.primary)
        ) {
            Text("Guardar producto", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}