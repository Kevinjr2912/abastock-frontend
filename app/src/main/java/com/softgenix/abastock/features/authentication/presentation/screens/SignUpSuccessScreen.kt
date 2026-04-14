package com.softgenix.abastock.features.authentication.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.core.ui.theme.CardBg
import com.softgenix.abastock.core.ui.theme.DividerClr
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.core.ui.theme.Surface
import com.softgenix.abastock.core.ui.theme.TextSec
import com.softgenix.abastock.features.authentication.presentation.components.SuccessCheckIcon
import com.softgenix.abastock.features.authentication.presentation.components.SuccessInfoRow
import com.softgenix.abastock.features.authentication.presentation.viewmodels.SignUpViewModel
import kotlinx.coroutines.delay

@Composable
fun SignUpSuccessScreen(
    onLoginSuccess: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    // Lanzar login automático al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.onSuccessScreenEntered()
        delay(5000L)
        viewModel.onAutoLogin()
    }

    // Justo cuando termine el login, ir a Home
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onLoginSuccess()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Header(modifier = Modifier.fillMaxSize()) {

                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .align(Alignment.TopEnd)
                            .offset(x = 25.dp, y = (-25).dp)
                            .background(Color.White.copy(alpha = 0.06f), CircleShape)
                    )

                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .offset(x = 5.dp, y = 60.dp)
                            .background(Color.White.copy(alpha = 0.05f), CircleShape)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .padding(horizontal = 18.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "¡Listo!",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "¡Bienvenido!",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Tu tienda está lista para empezar",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -(30).dp)
                    .background(
                        Surface,
                        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
                    .padding(horizontal = 18.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SuccessCheckIcon()

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "¡Cuenta creada!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Bienvenido a ABASTOCK,  Tienda ya está lista\npara operar",
                    fontSize = 13.sp,
                    color = TextSec,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )

                Spacer(Modifier.height(28.dp))

                // CARD DE INFO
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            CardBg,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(vertical = 4.dp)
                ) {

                    SuccessInfoRow(
                        iconRes = R.drawable.store_icon,
                        label = "Tienda",
                        value = state.storeName
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerClr
                    )

                    SuccessInfoRow(
                        iconRes = R.drawable.person_icon,
                        label = "Dueño",
                        value = state.name + " " + state.lastName
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerClr
                    )

                    SuccessInfoRow(
                        iconRes = R.drawable.call_icon,
                        label = "Teléfono",
                        value = state.phoneNumber
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = DividerClr
                    )

                    SuccessInfoRow(
                        iconRes = R.drawable.email_icon,
                        label = "Correo",
                        value = state.email
                    )
                }

                Spacer(Modifier.height(32.dp))

                // LOADING
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = NavyMid
                        )
                        Spacer(Modifier.width(10.dp))
                        Text("Entrando a tu tienda...")
                    }
                }
            }
        }
    }
}