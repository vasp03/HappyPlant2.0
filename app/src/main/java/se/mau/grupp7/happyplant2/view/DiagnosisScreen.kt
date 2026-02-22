package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import se.mau.grupp7.happyplant2.viewmodel.DiagnosisViewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.PaddingValues
import se.mau.grupp7.happyplant2.model.UserPlant
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem

private val ScreenBg = Color(0xFF23213E)
private val CardBg = Color(0xFF2C2A4A)

@Composable
fun DiagnosisScreen(
    userPlants: List<UserPlant>,
    preselectedPlantName: String = "",
    viewModel: DiagnosisViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(preselectedPlantName) {
        viewModel.loadRules(context)
        if (preselectedPlantName.isNotBlank()) {
            viewModel.selectPlantNameOnly(preselectedPlantName)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBg)
            .padding(16.dp)
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage != null -> {
                Text(
                    text = uiState.errorMessage!!,
                    color = Color.White
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "Diagnosis Tool",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    PlantPicker(
                        userPlants = userPlants,
                        selectedPlantName = uiState.selectedPlantName,
                        onSelect = { plant ->
                            viewModel.selectPlant(plant)
                        }
                    )

                    if (uiState.careInfoLoading) {
                        Text("Loading care info...", color = Color.White.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(8.dp))
                    } else if (uiState.careInfoText != null) {
                        Text(uiState.careInfoText!!, color = Color.White.copy(alpha = 0.85f))
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    //SEARCH INPUT
                    Text(
                        text = "Search symptoms...",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = uiState.symptomQuery,
                        onValueChange = { viewModel.updateSymptomQuery(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. yellow leaves") },
                        singleLine = true
                    )

                    //Suggestions when searching
                    if (uiState.suggestedSymptoms.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                uiState.suggestedSymptoms.forEach { suggestion ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.addSymptom(suggestion) }
                                            .padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = suggestion, color = Color.White)
                                    }
                                    Divider(color = Color.White.copy(alpha = 0.08f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Selected symptoms:",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.selectedSymptoms.isEmpty()) {
                        Text(
                            text = "No symptoms selected yet.",
                            color = Color.White.copy(alpha = 0.75f)
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(uiState.selectedSymptoms.toList()) { symptom ->
                                AssistChip(
                                    onClick = { viewModel.toggleSymptom(symptom) },
                                    label = {
                                        Text(
                                            text = symptom,
                                            color = Color.White
                                        )
                                    },
                                    colors = AssistChipDefaults.assistChipColors(
                                        containerColor = Color(0xFF3A375F),
                                        labelColor = Color.White
                                    )
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tip: tap a chip to remove it",
                            color = Color.White.copy(alpha = 0.65f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { viewModel.runDiagnosis() },
                            enabled = uiState.selectedSymptoms.isNotEmpty()
                        ) { Text("Diagnose") }

                        OutlinedButton(
                            onClick = { viewModel.clearSelection() },
                            enabled = uiState.selectedSymptoms.isNotEmpty()
                        ) { Text("Clear") }
                    }
                    Spacer(modifier = Modifier.height(20.dp))

                    //results
                    if (uiState.results.isNotEmpty()) {
                        val top = uiState.results.first()

                        Card(
                            colors = CardDefaults.cardColors(containerColor = CardBg),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                if (uiState.selectedPlantName != "No specific plant") {
                                    Text(
                                        text = "Diagnosis for ${uiState.selectedPlantName}",
                                        color = Color.White.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }

                                Text(
                                    text = "Most likely issue",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = top.rule.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Tips",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                top.rule.tips.forEach { tip ->
                                    Text(text = "· $tip", color = Color.White)
                                }
                            }
                        }
                    } else if (uiState.selectedSymptoms.isNotEmpty()) {
                        Text(
                            text = "No matches yet. Try different symptoms.",
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlantPicker(
    userPlants: List<UserPlant>,
    selectedPlantName: String,
    onSelect: (UserPlant?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedPlantName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Choose plant (optional)") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("No specific plant") },
                onClick = {
                    expanded = false
                    onSelect(null)
                }
            )

            userPlants.forEach { plant ->
                DropdownMenuItem(
                    text = { Text(plant.name) },
                    onClick = {
                        expanded = false
                        onSelect(plant)
                    }
                )
            }
        }
    }
}