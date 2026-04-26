package com.example.tharwa.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tharwa.api.FoodResponse
import com.example.tharwa.api.LocalAIAnalyzer
import com.example.tharwa.ui.theme.TharwaTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

// Demonstration Activity: Scan -> Sanitize -> AI Check -> Result
class ScanResultDemoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val aiAnalyzer = LocalAIAnalyzer(this)

        setContent {
            TharwaTheme {
                var barcodeInput by remember { mutableStateOf("737628064502") }
                var result by remember { mutableStateOf<FoodResponse?>(null) }
                var isProcessing by remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Secure Local Scan Demo", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = barcodeInput,
                        onValueChange = { barcodeInput = it },
                        label = { Text("Simulated Scan Input") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isProcessing) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {
                                isProcessing = true
                                MainScope().launch {
                                    // 1. Locally Process (No data leaves device)
                                    // 2. Sanitize & AI check inside analyzeFoodLocally
                                    result = aiAnalyzer.analyzeFoodLocally(barcodeInput)
                                    isProcessing = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Process Locally")
                        }
                    }

                    result?.let {
                        Spacer(modifier = Modifier.height(32.dp))
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Status: ${it.status.uppercase()}", style = MaterialTheme.typography.titleLarge)
                                Text("Reason: ${it.reason}")
                                Text("Recommendation: ${it.recommendation}")
                            }
                        }
                    }
                }
            }
        }
    }
}
