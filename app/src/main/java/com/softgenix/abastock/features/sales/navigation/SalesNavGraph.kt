package com.softgenix.abastock.features.sales.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.softgenix.abastock.core.navigation.Cart
import com.softgenix.abastock.core.navigation.FeatureNavGraph
import com.softgenix.abastock.core.navigation.ProductPicker
import com.softgenix.abastock.core.navigation.SalesHistory
import com.softgenix.abastock.core.navigation.Success
import com.softgenix.abastock.features.sales.presentation.screens.ProductSelectionScreen
import com.softgenix.abastock.features.sales.presentation.screens.SalesHistoryScreen
import com.softgenix.abastock.features.sales.presentation.screens.SalesScreen
import com.softgenix.abastock.features.sales.presentation.screens.SuccessSaleScreen

class SalesNavGraph : FeatureNavGraph{

    override fun registerNavGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController
    ) {

        navGraphBuilder.composable<SalesHistory> {
            SalesHistoryScreen(
                onNavigateBack = {
                    navController.navigate(ProductPicker)
                },
                onNavigateToNewSale = {
                    navController.navigate(Cart)
                }
            )
        }

        navGraphBuilder.composable<Cart> {
            SalesScreen(
                navController = navController,
                onNavigateToSelection = {
                    navController.navigate(ProductPicker)
                },
                onNavigateToSuccess = { total ->
                    navController.navigate(Success(total))
                }
            )
        }

        navGraphBuilder.composable<ProductPicker> {
            ProductSelectionScreen(
                onBack = { navController.popBackStack() },
                onProductSelected = {
                    navController.popBackStack()
                }
            )
        }

        navGraphBuilder.composable<Success> { backStackEntry ->
            val route: Success = backStackEntry.toRoute()
            SuccessSaleScreen(
                total = route.total,
                onDismiss = {
                    navController.navigate(Cart) {
                        popUpTo<Cart> { inclusive = true }
                    }
                }
            )
        }
    }
}
