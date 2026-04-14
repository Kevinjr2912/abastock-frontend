package com.softgenix.abastock.features.authentication.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.softgenix.abastock.core.navigation.FeatureNavGraph
import com.softgenix.abastock.core.navigation.Inventory
import com.softgenix.abastock.core.navigation.Login
import com.softgenix.abastock.core.navigation.Register
import com.softgenix.abastock.core.navigation.RegisterGraph
import com.softgenix.abastock.core.navigation.SuccessRegister
import com.softgenix.abastock.features.authentication.presentation.screens.SignInScreen
import com.softgenix.abastock.features.authentication.presentation.screens.SignUpScreen
import com.softgenix.abastock.features.authentication.presentation.screens.SignUpSuccessScreen
import com.softgenix.abastock.features.authentication.presentation.viewmodels.SignUpViewModel

class AuthNavGraph : FeatureNavGraph {
    override fun registerNavGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {

        navGraphBuilder.composable<Login> {
            SignInScreen(
                onNavigateToRegister = { navController.navigate(RegisterGraph) },
                onLoginSuccess = {
                    navController.navigate(Inventory) {
                        popUpTo<Login> { inclusive = true }
                    }
                })
        }

        // Nested graph — comparte el mismo ViewModel
        navGraphBuilder.navigation<RegisterGraph>(startDestination = Register) {

            composable<Register> { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<RegisterGraph>()
                }
                val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
                SignUpScreen(
                    onNavigateToLogin = { navController.navigate(Login) },
                    onSignUpSuccess = { navController.navigate(SuccessRegister) },
                    viewModel = viewModel
                )
            }

            composable<SuccessRegister> { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<RegisterGraph>()
                }
                val viewModel: SignUpViewModel = hiltViewModel(parentEntry)
                SignUpSuccessScreen(
                    onLoginSuccess = {
                        navController.navigate(Inventory) {
                            popUpTo<Login> { inclusive = true }
                        }
                    },
                    viewModel = viewModel
                )
            }
        }
    }
}