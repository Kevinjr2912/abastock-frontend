package com.softgenix.abastock.features.inventory.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.softgenix.abastock.core.navigation.AddToCart
import com.softgenix.abastock.core.navigation.CreateProduct
import com.softgenix.abastock.core.navigation.FeatureNavGraph
import com.softgenix.abastock.core.navigation.Inventory
import com.softgenix.abastock.core.navigation.PurchaseEmptyCart
import com.softgenix.abastock.features.inventory.presentation.screens.CreateProductScreen
import com.softgenix.abastock.features.inventory.presentation.screens.InventoryScreen


// esta sera la primer forma de navegar ( aun se cambiare, presentar a ali) la segunda forma
//se encuentrea el de auth
class InventoryNavGraph: FeatureNavGraph {
    override fun registerNavGraph(navGraphBuilder: NavGraphBuilder, navController: NavHostController) {
        navGraphBuilder.composable<Inventory>{
            InventoryScreen(
                navController = navController,
                onAddPurchaseClick = {
                    navController.navigate(PurchaseEmptyCart)
                }
            )
        }

        navGraphBuilder.composable<CreateProduct> { backStackEntry ->
            val route: CreateProduct = backStackEntry.toRoute()
            CreateProductScreen(
                barcode = route.barcode ?: "",
                onProductCreated = { barcode ->
                    navController.navigate(AddToCart(barcode))
                }
            )
        }

    }
}