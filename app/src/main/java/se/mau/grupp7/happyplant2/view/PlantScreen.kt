package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.model.Defect
import se.mau.grupp7.happyplant2.model.UserPlant
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantScreen(
    plant: UserPlant,
    onRemove: (UserPlant) -> Unit,
    onWater: (UserPlant) -> Unit,
    onCategoryChange: (UserPlant, String) -> Unit,
    onDefectChange: (UserPlant, Defect) -> Unit,
    defectOptions: List<Defect>,
    onClose: () -> Unit
) {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val lastWateredTime by remember(plant.lastTimeWatered) {
        mutableStateOf(dateFormat.format(plant.lastTimeWatered))
    }

    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember {
        mutableStateOf(plant.category.ifEmpty { "Unassigned" })
    }
    val categories = listOf("Living Room", "Kitchen", "Bedroom", "Unassigned")

    var defectExpanded by remember { mutableStateOf(false) }
    var selectedDefect by remember { mutableStateOf(plant.defect) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF23213E))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            AsyncImage(
                model = plant.imageURL,
                contentDescription = plant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = plant.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White
            )

            Text(
                text = plant.description,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Last watered: $lastWateredTime",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Text(
                text = "Health: ${plant.healthStatus}/5",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )

            Text(text = "Family: ${plant.family}", color = Color.White)
            Text(text = "Sunlight Needs: ${plant.sunlight}", color = Color.White)
            Text(text = "Water Needs: ${plant.wateringNeeds}", color = Color.White)

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Category Dropdown
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                TextField(
                    value = selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                selectedCategory = category
                                categoryExpanded = false
                                onCategoryChange(plant, category)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Defect Dropdown
            ExposedDropdownMenuBox(
                expanded = defectExpanded,
                onExpandedChange = { defectExpanded = !defectExpanded }
            ) {
                TextField(
                    value = selectedDefect.displayName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Defect") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = defectExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = defectExpanded,
                    onDismissRequest = { defectExpanded = false }
                ) {
                    defectOptions.forEach { defect ->
                        DropdownMenuItem(
                            text = { Text(defect.displayName) },
                            onClick = {
                                selectedDefect = defect
                                defectExpanded = false
                                onDefectChange(plant, defect)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onWater(plant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Water Plant")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onRemove(plant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove Plant")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        // ✅ Floating Back Arrow
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(12.dp)
                .size(48.dp)
                .background(
                    Color.Black.copy(alpha = 0.4f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}