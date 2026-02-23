package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.R
import se.mau.grupp7.happyplant2.SearchMode
import se.mau.grupp7.happyplant2.model.PestDisease
import se.mau.grupp7.happyplant2.model.PlantDetails

@Composable
fun DiscoverSearchScreen(
    plantTypes: List<PlantDetails>,
    suggestions: List<String>,
    diseases: List<PestDisease>,
    onSearchPlants: (String) -> Unit,
    onLoadDiseases: () -> Unit,
    onAdd: (PlantDetails, Int) -> Unit,
    isLoading: Boolean
) {
    var mode by rememberSaveable { mutableStateOf(SearchMode.PLANTS) }
    var text by rememberSaveable { mutableStateOf("") }

    val filteredDiseases = remember(text, diseases) {
        if (text.isBlank()) diseases
        else diseases.filter {
            it.common_name?.contains(text, ignoreCase = true) == true ||
                    it.scientific_name?.contains(text, ignoreCase = true) == true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(if (mode == SearchMode.PLANTS) "Search for plants" else "Search diagnoses")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("search_text_field"),
            trailingIcon = {
                IconButton(modifier = Modifier.testTag("search_text_search_button"), onClick = {
                    if (mode == SearchMode.PLANTS) onSearchPlants(text)
                }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (mode == SearchMode.PLANTS) onSearchPlants(text)
                }
            )
        )

        SearchModePills(
            mode = mode,
            onModeChange = {
                mode = it
                if (it == SearchMode.DIAGNOSES) onLoadDiseases()
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.weight(1f)) {
            when (mode) {
                SearchMode.PLANTS -> PlantDiscoverContent(
                    plantTypes = plantTypes,
                    suggestions = suggestions,
                    onSuggestionClick = { s ->
                        text = s
                        onSearchPlants(s)
                    },
                    onAdd = { plant, days -> onAdd(plant, days) },
                    isLoading = isLoading
                )

                SearchMode.DIAGNOSES -> DiagnosesDiscoverContent(
                    diseases = filteredDiseases,
                    isLoading = isLoading
                )
            }
        }
    }
}

@Composable
private fun SearchModePills(
    mode: SearchMode,
    onModeChange: (SearchMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        val isPlants = mode == SearchMode.PLANTS

        // PLANTS BUTTON
        Button(
            onClick = { onModeChange(SearchMode.PLANTS) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPlants) Color.White else Color(0xFF3F51B5),
                contentColor = if (isPlants) Color.Black else Color.White
            ),
            border = if (isPlants) BorderStroke(1.dp, Color.Black) else null
        ) {
            Text("Plants")
        }

        // DIAGNOSES BUTTON
        Button(
            onClick = { onModeChange(SearchMode.DIAGNOSES) },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isPlants) Color.White else Color(0xFF3F51B5),
                contentColor = if (!isPlants) Color.Black else Color.White
            ),
            border = if (!isPlants) BorderStroke(1.dp, Color.Black) else null
        ) {
            Text("Diagnoses")
        }
    }
}

@Composable
fun DiagnosesDiscoverContent(
    diseases: List<PestDisease>,
    isLoading: Boolean
) {
    var selectedDisease by remember { mutableStateOf<PestDisease?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(diseases) { disease ->
                    DiseaseCard(disease) { clicked ->
                        selectedDisease = clicked
                    }
                }
            }
        }

        // Dialog for details
        selectedDisease?.let { disease ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = disease.common_name ?: "Unknown",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    disease.scientific_name?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (!disease.description.isNullOrEmpty()) {
                        Text("Description", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.9f))
                        Spacer(modifier = Modifier.height(4.dp))
                        disease.description.forEach { section ->
                            Text(
                                text = "${section.subtitle ?: ""}\n${section.description ?: ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (!disease.solution.isNullOrEmpty()) {
                        Text("Solution", style = MaterialTheme.typography.titleMedium, color = Color.White.copy(alpha = 0.9f))
                        Spacer(modifier = Modifier.height(4.dp))
                        disease.solution.forEach { section ->
                            Text(
                                text = "${section.subtitle ?: ""}\n${section.description ?: ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { selectedDisease = null }) {
                        Text("Close", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun PlantDiscoverContent(
    plantTypes: List<PlantDetails>,
    suggestions: List<String>,
    onSuggestionClick: (String) -> Unit,
    onAdd: (PlantDetails, Int) -> Unit,
    isLoading: Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedPlant by remember { mutableStateOf<PlantDetails?>(null) }
    var daysAgoText by remember { mutableStateOf("0") }

    Box(modifier = Modifier.fillMaxSize()) {

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {

                if (suggestions.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Did you mean:",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        suggestions.forEach { s ->
                            Button(onClick = { onSuggestionClick(s) }) {
                                Text(s)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(plantTypes) { plantType ->
                        PlantCard(
                            plantType,
                            onAdd = {
                                selectedPlant = plantType
                                daysAgoText = "0"
                                showDialog = true
                            }
                        )
                    }
                }

                if (showDialog && selectedPlant != null) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("When was it last watered?") },
                        text = {
                            Column {
                                Text("Enter number of days ago (0 = today)")
                                Spacer(modifier = Modifier.height(8.dp))
                                TextField(
                                    value = daysAgoText,
                                    onValueChange = {
                                        if (it.all { char -> char.isDigit() }) daysAgoText = it
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                            }
                        },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val days = daysAgoText.toIntOrNull() ?: 0
                                    onAdd(selectedPlant!!, days)
                                    showDialog = false
                                }
                            ) { Text("Add Plant") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PlantCard(plantDetails: PlantDetails, onAdd: (PlantDetails) -> Unit) {

    fun fmt(value : String?): String = value?.takeIf { it.isNotBlank() } ?: "Unknown"

    Card(modifier = Modifier.padding(8.dp).testTag("PlantCardResult")) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = plantDetails.imageUrl,
                contentDescription = plantDetails.common_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.plant_placeholder),
                error = painterResource(R.drawable.plant_placeholder),
                fallback = painterResource(R.drawable.plant_placeholder)

            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(text = fmt(plantDetails.common_name))
                    Text(text = fmt(plantDetails.scientific_name))
                    Text(text = "Genus: ${fmt(plantDetails.genus)}")
                }
            }

            Button(
                onClick = { onAdd(plantDetails) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Plant")
            }
        }
    }
}

@Composable
fun DiseaseCard(
    disease: PestDisease,
    onClick: (PestDisease) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentHeight() // Take only needed height
            .clickable { onClick(disease) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            disease.images?.firstOrNull()?.medium_url?.let { url ->
                AsyncImage(
                    model = url,
                    contentDescription = disease.common_name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    contentScale = ContentScale.Crop,
                    error = painterResource(R.drawable.plant_placeholder),
                    fallback = painterResource(R.drawable.plant_placeholder)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = disease.common_name ?: "Unknown issue",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = disease.scientific_name ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}