package com.example.tharwa.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tharwa.data.FoodBatch
import com.example.tharwa.ui.MainViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBatchScreen(
    viewModel: MainViewModel,
    onBatchSaved: (String) -> Unit,
    onBack: () -> Unit
) {
    var cropType by remember { mutableStateOf("") }
    var origin by remember { mutableStateOf("Manual Entry") }
    var certification by remember { mutableStateOf("None") }
    var expirationDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val certifications = listOf("None", "Organic", "Bio", "Fair Trade", "Global G.A.P")
    var expandedCert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register New Batch") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Inventory,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = cropType,
                onValueChange = { cropType = it },
                label = { Text("Crop Type (e.g. Tomato, Apple)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it },
                label = { Text("Origin / Farm Location") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expandedCert,
                onExpandedChange = { expandedCert = !expandedCert },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = certification,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Certification") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCert) },
                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedCert,
                    onDismissRequest = { expandedCert = false }
                ) {
                    certifications.forEach { cert ->
                        DropdownMenuItem(
                            text = { Text(cert) },
                            onClick = {
                                certification = cert
                                expandedCert = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = expirationDate,
                onValueChange = { expirationDate = it },
                label = { Text("Expiration Date (Optional)") },
                leadingIcon = { Icon(Icons.Default.Event, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("YYYY-MM-DD") }
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Text("Registering on Blockchain...", modifier = Modifier.padding(top = 8.dp))
            } else {
                Button(
                    onClick = {
                        if (cropType.isNotBlank()) {
                            isLoading = true
                            val batchId = "THW-" + System.currentTimeMillis().toString().takeLast(6)
                            val newBatch = FoodBatch(
                                batchId = batchId,
                                cropType = cropType,
                                origin = origin,
                                certification = certification,
                                expirationDate = expirationDate,
                                farmerUsername = viewModel.currentUser.value?.username ?: "Farmer"
                            )
                            viewModel.registerBatch(newBatch) {
                                isLoading = false
                                onBatchSaved(batchId)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Register Batch to Blockchain", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}
