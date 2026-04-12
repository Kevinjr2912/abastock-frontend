package com.softgenix.abastock.features.purchases.navigation

import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.softgenix.abastock.core.hardware.domain.ScannerManager
import com.softgenix.abastock.core.navigation.*
import com.softgenix.abastock.features.purchases.presentation.screens.AddToCartScreen
import com.softgenix.abastock.features.purchases.presentation.screens.PurchaseEmptyCartScreen
import com.softgenix.abastock.features.purchases.presentation.screens.PurchaseScannerScreen
import com.softgenix.abastock.features.purchases.presentation.screens.PurchaseSummaryScreen
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
object PurchasesGraph

class PurchasesNavGraph @Inject constructor(
    private val scannerManager: ScannerManager
) : FeatureNavGraph {
    override fun registerNavGraph(
        navGraphBuilder: NavGraphBuilder,
        navController: NavHostController
    ) {
        navGraphBuilder.navigation<PurchasesGraph>(
            startDestination = PurchaseEmptyCart
        ) {
            composable<PurchaseEmptyCart> {
                PurchaseEmptyCartScreen(onNavigateToScanner = {
                    navController.navigate(PurchaseScanner(storeId = ""))
                })
            }

            composable<PurchaseScanner> { backStackEntry ->
                PurchaseScannerScreen(
                    scannerManager = scannerManager,
                    onNavigateToCreate = { barcode -> navController.navigate(CreateProduct(barcode)) },
                    onNavigateToExisting = { barcode -> navController.navigate(AddToCart(barcode)) }
                )
            }

            composable<AddToCart> { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<PurchasesGraph>()
                }

                AddToCartScreen(
                    barcode = backStackEntry.toRoute<AddToCart>().barcode,
                    supplyViewModel = hiltViewModel(parentEntry),
                    onAddedToCart = { id ->
                        navController.navigate(PurchaseSummary(storeId = id))
                    }
                )
            }

            composable<PurchaseSummary> { backStackEntry ->
                val route = backStackEntry.toRoute<PurchaseSummary>()
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<PurchasesGraph>()
                }

                PurchaseSummaryScreen(
                    storeId = route.storeId,
                    viewModel = hiltViewModel(parentEntry),
                    onFinish = {
                            navController.navigate(Inventory) {
                                popUpTo<PurchasesGraph> { inclusive = true }
                            }
                    },
                    onScanAnother = {
                        navController.navigate(PurchaseScanner(storeId = route.storeId)) {
                            popUpTo<PurchaseScanner> { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}