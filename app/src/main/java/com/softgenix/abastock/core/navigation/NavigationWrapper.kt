package com.softgenix.abastock.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.softgenix.abastock.core.data.local.SessionEvent
import com.softgenix.abastock.core.data.local.SessionEventBus
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NavigationWrapper(
    navGraphs: List<FeatureNavGraph>,
    startDestination: Any = Login,
    sessionEventBus: SessionEventBus
) {

    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        sessionEventBus.events.collectLatest { event ->
            if (event is SessionEvent.Logout) {
                navController.navigate(Login) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navGraphs.forEach { graph ->
            graph.registerNavGraph(this, navController)
        }
    }
}