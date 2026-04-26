package com.example.tharwa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tharwa.ui.MainViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferencesScreen(
    onSave: () -> Unit,
    viewModel: MainViewModel
) {
    val commonAllergies = listOf("Peanuts", "Dairy", "Gluten", "Soy", "Eggs", "Seafood", "Tree Nuts")
    val dietaryLifestyles = listOf("Vegan", "Vegetarian", "Keto", "Paleo", "Halal", "Kosher")
    val healthGoals = listOf("Low Sugar", "Low Sodium", "High Protein", "Organic Only")

    val selectedAllergies = remember { mutableStateListOf<String>() }
    var selectedDiet by remember { mutableStateOf("Standard") }
    val selectedGoals = remember { mutableStateListOf<String>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Personalize Tharwa",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                "We customize scan results to your health",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    
                    PreferenceSectionTitle("Allergies & Intolerances")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        commonAllergies.forEach { allergy ->
                            FilterChip(
                                selected = selectedAllergies.contains(allergy),
                                onClick = {
                                    if (selectedAllergies.contains(allergy)) selectedAllergies.remove(allergy)
                                    else selectedAllergies.add(allergy)
                                },
                                label = { Text(allergy) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PreferenceSectionTitle("Dietary Lifestyle")
                    dietaryLifestyles.forEach { diet ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedDiet == diet,
                                onClick = { selectedDiet = diet }
                            )
                            Text(diet, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    PreferenceSectionTitle("Health & Nutritional Goals")
                    healthGoals.forEach { goal ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = selectedGoals.contains(goal),
                                onCheckedChange = {
                                    if (selectedGoals.contains(goal)) selectedGoals.remove(goal)
                                    else selectedGoals.add(goal)
                                }
                            )
                            Text(goal, style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val allergiesStr = selectedAllergies.joinToString(",")
                            val goalsStr = selectedGoals.joinToString(",")
                            viewModel.updatePreferences(allergiesStr, selectedDiet, goalsStr)
                            onSave()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Finish Setup", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                }
            }
        }
    }
}

@Composable
fun PreferenceSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}
