package com.example.tharwa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tharwa.data.AppDatabase
import com.example.tharwa.repository.AppRepository
import com.example.tharwa.ui.MainViewModel
import com.example.tharwa.ui.ViewModelFactory
import com.example.tharwa.ui.screens.*
import com.example.tharwa.ui.theme.TharwaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val database = AppDatabase.getDatabase(this)
        val repository = AppRepository(
            database.userDao(), 
            database.foodEntryDao(), 
            database.foodBatchDao(),
            database.marketplaceItemDao(),
            database.offerDao()
        )
        val viewModelFactory = ViewModelFactory(repository, this)

        setContent {
            TharwaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val viewModel: MainViewModel = viewModel(factory = viewModelFactory)
                    AppNavigation(viewModel)
                }
            }
        }
    }
}

@Composable
fun AppNavigation(viewModel: MainViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                onNavigateToSignUp = { navController.navigate("signup") },
                onLoginSuccess = { 
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = { 
                    val user = viewModel.currentUser.value
                    if (user?.role == "Farmer") {
                        navController.navigate("home") {
                            popUpTo("signup") { inclusive = true }
                        }
                    } else {
                        navController.navigate("preferences")
                    }
                },
                viewModel = viewModel
            )
        }
        composable("preferences") {
            PreferencesScreen(
                onSave = { 
                    navController.navigate("home") {
                        popUpTo("preferences") { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
        composable("home") {
            HomeScreen(
                onScanClick = { navController.navigate("scan") },
                onHistoryClick = { 
                    val user = viewModel.currentUser.value
                    if (user?.role == "Farmer") {
                        navController.navigate("farmer_dashboard")
                    } else {
                        navController.navigate("history")
                    }
                },
                onAddEntryClick = { navController.navigate("add_batch") },
                onMarketplaceClick = { navController.navigate("marketplace") },
                onOffersClick = { navController.navigate("farmer_offers") },
                onSignOut = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                viewModel = viewModel
            )
        }
        composable("scan") {
            ScanScreen(
                onResult = { barcode -> 
                    navController.navigate("result/$barcode")
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("result/{barcode}") { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            ResultScreen(
                barcode = barcode,
                viewModel = viewModel,
                onBack = { navController.navigate("home") }
            )
        }
        composable("history") {
            HistoryScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
        // Farmer Module Screens
        composable("farmer_dashboard") {
            FarmerDashboard(
                viewModel = viewModel,
                onAddBatchClick = { navController.navigate("add_batch") },
                onViewQR = { batchId -> navController.navigate("view_qr/$batchId") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("add_batch") {
            AddBatchScreen(
                viewModel = viewModel,
                onBatchSaved = { batchId -> 
                    navController.navigate("view_qr/$batchId") {
                        popUpTo("farmer_dashboard")
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("view_qr/{batchId}") { backStackEntry ->
            val batchId = backStackEntry.arguments?.getString("batchId") ?: ""
            ViewBatchQRScreen(
                batchId = batchId,
                viewModel = viewModel,
                onBack = { 
                    navController.navigate("farmer_dashboard") {
                        popUpTo("farmer_dashboard") { inclusive = true }
                    }
                }
            )
        }
        // Marketplace Screens
        composable("marketplace") {
            MarketplaceScreen(
                viewModel = viewModel,
                onAddProductClick = { navController.navigate("add_product") },
                onBack = { navController.popBackStack() }
            )
        }
        composable("add_product") {
            AddProductScreen(
                viewModel = viewModel,
                onProductSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable("farmer_offers") {
            FarmerOffersScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
