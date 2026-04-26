package com.example.tharwa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tharwa.data.FoodEntry
import com.example.tharwa.ui.MainViewModel
import com.example.tharwa.ui.theme.DangerRed
import com.example.tharwa.ui.theme.RiskyOrange
import com.example.tharwa.ui.theme.SafeGreen

@Composable
fun ResultScreen(
    barcode: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    var result by remember { mutableStateOf<FoodEntry?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(barcode) {
        viewModel.scanFood(barcode) { entry ->
            result = entry
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Analyzing food composition...", style = MaterialTheme.typography.bodyLarge)
        } else {
            result?.let { entry ->
                val (statusColor, icon) = when (entry.status.lowercase()) {
                    "safe" -> SafeGreen to Icons.Default.CheckCircle
                    "risky" -> RiskyOrange to Icons.Default.Warning
                    else -> DangerRed to Icons.Default.Info
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(80.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = entry.status.uppercase(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = statusColor,
                            fontWeight = FontWeight.Black
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = entry.foodName,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray.copy(alpha = 0.5f))
                        
                        Row(verticalAlignment = Alignment.Top) {
                            Text("Reason: ", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                            Text(entry.reason, style = MaterialTheme.typography.bodyLarge)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(statusColor.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                                .padding(16.dp)
                        ) {
                            Text(
                                text = entry.recommendation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = statusColor,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
                
                Button(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Return Home", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
