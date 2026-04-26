package com.example.tharwa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tharwa.ui.MainViewModel

@Composable
fun HomeScreen(
    onScanClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onAddEntryClick: () -> Unit,
    onMarketplaceClick: () -> Unit,
    onOffersClick: () -> Unit,
    onSignOut: () -> Unit,
    viewModel: MainViewModel
) {
    val user = viewModel.currentUser.value
    val isFarmer = user?.role == "Farmer"

    Scaffold(
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.surface,
                actions = {
                    IconButton(onClick = onHistoryClick) {
                        Icon(if (isFarmer) Icons.Default.Dashboard else Icons.AutoMirrored.Filled.List, contentDescription = "History")
                    }
                    IconButton(onClick = onMarketplaceClick) {
                        Icon(Icons.Default.Storefront, contentDescription = "Marketplace")
                    }
                    if (isFarmer) {
                        IconButton(onClick = onOffersClick) {
                            Icon(Icons.Default.ShoppingBag, contentDescription = "Offers")
                        }
                    }
                    IconButton(onClick = { viewModel.signOut(onSignOut) }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Sign Out", tint = Color.Red)
                    }
                },
                floatingActionButton = {
                    if (!isFarmer) {
                        FloatingActionButton(
                            onClick = onScanClick,
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.elevation()
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Scan")
                        }
                    } else {
                        FloatingActionButton(
                            onClick = onAddEntryClick,
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            elevation = FloatingActionButtonDefaults.elevation()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Batch")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            if (isFarmer) 
                                listOf(MaterialTheme.colorScheme.tertiary, MaterialTheme.colorScheme.tertiaryContainer)
                            else 
                                listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                        ),
                        RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(32.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Icon(
                            if (isFarmer) Icons.Default.Agriculture else Icons.Default.Person,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Welcome back,",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            user?.username ?: "User",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            user?.role ?: "",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Explore Tharwa",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                if (isFarmer) {
                    HomeActionCard(
                        title = "Farmer Dashboard",
                        subtitle = "Manage your blockchain batches",
                        icon = Icons.Default.Dashboard,
                        color = MaterialTheme.colorScheme.tertiary,
                        onClick = onHistoryClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    HomeActionCard(
                        title = "Order Requests",
                        subtitle = "Manage purchase offers from users",
                        icon = Icons.Default.ShoppingBag,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onOffersClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                } else {
                    HomeActionCard(
                        title = "Scan Food",
                        subtitle = "Check ingredients for allergens",
                        icon = Icons.Default.CameraAlt,
                        color = MaterialTheme.colorScheme.primary,
                        onClick = onScanClick
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                HomeActionCard(
                    title = "Marketplace",
                    subtitle = "Buy & sell fresh produce",
                    icon = Icons.Default.Storefront,
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = onMarketplaceClick
                )

                if (!isFarmer) {
                    Spacer(modifier = Modifier.height(16.dp))
                    HomeActionCard(
                        title = "My History",
                        subtitle = "View your scan history",
                        icon = Icons.AutoMirrored.Filled.List,
                        color = Color.Gray,
                        onClick = onHistoryClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(50.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
