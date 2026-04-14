package com.softgenix.abastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.softgenix.abastock.core.navigation.NavigationWrapper
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.softgenix.abastock.core.ui.theme.AbastockTheme
import com.softgenix.abastock.features.home.navigation.HomeNavGraph
import com.softgenix.abastock.features.purchases.navigation.PurchasesNavGraph
import com.softgenix.abastock.features.sales.navigation.SalesNavGraph
import com.softgenix.abastock.features.authentication.navigation.AuthNavGraph
import com.softgenix.abastock.features.authentication.presentation.screens.SignUpScreen
import com.softgenix.abastock.features.inventory.navigation.InventoryNavGraph
import com.softgenix.abastock.features.authentication.presentation.screens.SignUpSuccessScreen
import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.data.local.SessionEventBus
import com.softgenix.abastock.core.navigation.Home
import com.softgenix.abastock.core.navigation.Login
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var purchasesGraph: PurchasesNavGraph
    @Inject lateinit var tokenManager: TokenManager
    @Inject lateinit var sessionEventBus: SessionEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val navGraphs = listOf(
            InventoryNavGraph(),
            AuthNavGraph(),
            HomeNavGraph(),
            purchasesGraph,
            SalesNavGraph()
        )

        val startDestination = if (tokenManager.getAccessToken() != null) Home else Login

        setContent {
            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = Color.Transparent,
                    darkIcons = false
                )
            }

            AbastockTheme {
                NavigationWrapper(
                    navGraphs = navGraphs,
                    startDestination = startDestination,
                    sessionEventBus = sessionEventBus
                )
            }
        }
    }
}