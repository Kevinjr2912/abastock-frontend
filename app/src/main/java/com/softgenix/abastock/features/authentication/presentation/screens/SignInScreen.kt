package com.softgenix.abastock.features.authentication.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.softgenix.abastock.R
import com.softgenix.abastock.core.shared.components.Button
import com.softgenix.abastock.core.shared.components.Header
import com.softgenix.abastock.core.shared.components.HeaderBubble
import com.softgenix.abastock.core.shared.components.InputLabel
import com.softgenix.abastock.core.shared.components.StyledInput
import com.softgenix.abastock.core.shared.entities.Bubble
import com.softgenix.abastock.core.ui.theme.AccentGold
import com.softgenix.abastock.core.ui.theme.CardBg
import com.softgenix.abastock.core.ui.theme.DividerClr
import com.softgenix.abastock.core.ui.theme.NavyMid
import com.softgenix.abastock.core.ui.theme.Surface
import com.softgenix.abastock.core.ui.theme.TextPrim
import com.softgenix.abastock.core.ui.theme.TextSec
import com.softgenix.abastock.features.authentication.presentation.components.StatDot
import com.softgenix.abastock.features.authentication.presentation.components.StatItem
import com.softgenix.abastock.features.authentication.presentation.viewmodels.SignInViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignInScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) {
            onLoginSuccess()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
            .navigationBarsPadding()
    ) {
        // HEADER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Header(
                    modifier = Modifier.fillMaxSize()
                ) {

                    HeaderBubble(
                        Bubble(130, 30, 20),
                        Bubble(180, -40, -40)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.15f),
                                    RoundedCornerShape(50)
                                )
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "ABASTOCK",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                letterSpacing = 2.sp
                            )
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "El control de tu tienda en tu bolsillo",
                            color = Color.White.copy(alpha = 0.65f),
                            fontSize = 13.sp
                        )

                        Spacer(Modifier.height(20.dp))

                        Row(
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.10f),
                                    RoundedCornerShape(24.dp)
                                )
                                .padding(horizontal = 24.dp, vertical = 5.dp),
                            horizontalArrangement = Arrangement.spacedBy(15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatItem("100+", "Tiendas", AccentGold)
                            StatDot()
                            StatItem("4.9 ★", "Calificación", AccentGold)
                            StatDot()
                            StatItem("MX", "LATAM", AccentGold)
                        }
                    }
                }
            }
        }

        // FORMULARIO
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -(30).dp)
                    .background(
                        Surface,
                        RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                    )
                    .padding(horizontal = 15.dp, vertical = 25.dp)
            ) {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrim
                )
                Text(
                    text = "Bienvenido de vuelta, tendero",
                    color = TextSec,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(Modifier.height(15.dp))

                InputLabel("Teléfono o correo")
                Spacer(Modifier.height(3.dp))
                StyledInput(
                    value = state.credential,
                    onValueChange = viewModel::onCredentialChange,
                    placeholder = "55 1234 5678",
                    leadingIconRes = R.drawable.call_icon
                )

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    InputLabel("Contraseña")
                    Text(
                        text = "¿Olvidaste tu contraseña?",
                        color = NavyMid,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(Modifier.height(3.dp))

                StyledInput(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    placeholder = "Mínimo 8 caracteres",
                    leadingIconRes = R.drawable.icon_password,
                    isPassword = true,
                    passwordVisible = state.passwordVisible,
                    onTogglePassword = viewModel::onTogglePasswordVisible
                )

                Spacer(Modifier.height(22.dp))

                Button(
                    nameAction = "Iniciar sesión",
                    colors = ButtonColors(
                        containerColor = NavyMid,
                        contentColor = Color.White,
                        disabledContainerColor = NavyMid.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = viewModel::onSignIn,
                )

                state.error?.let {
                    Spacer(Modifier.height(10.dp))

                    Text(
                        text = it,
                        color = Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(15.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DividerClr)
                    Text(text = "  o continúa con  ", color = TextSec, fontSize = 12.sp)
                    HorizontalDivider(modifier = Modifier.weight(1f), color = DividerClr)
                }

                Spacer(Modifier.height(15.dp))

                Button(
                    nameAction = "Google",
                    icon = R.drawable.google_icon,
                    outlined = true,
                    colors = ButtonColors(
                        containerColor = CardBg,
                        contentColor = TextPrim,
                        disabledContainerColor = CardBg,
                        disabledContentColor = TextSec
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(15.dp))

                Button(
                    nameAction = "Apple",
                    icon = R.drawable.apple_icon,
                    colors = ButtonColors(
                        containerColor = Color(0xFF0A0A0A),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF0A0A0A),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextSec, fontSize = 12.sp)) {
                            append("¿Aún no tienes cuenta?  ")
                        }
                        withStyle(
                            SpanStyle(
                                color = NavyMid,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Regístrate gratis")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {onNavigateToRegister() },
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}