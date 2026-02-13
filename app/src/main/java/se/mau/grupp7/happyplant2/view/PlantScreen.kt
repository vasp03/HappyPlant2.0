package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    defectOptions: List<Defect>
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val lastWateredTime by remember(plant.lastTimeWatered) { mutableStateOf(dateFormat.format(plant.lastTimeWatered)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
        Text(text = plant.name, style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Text(text = plant.description, style = MaterialTheme.typography.titleMedium, color = Color.White)
        Text(
            text = "Category: ${plant.category.ifEmpty { "Unassigned" }}",
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(text = "Last watered: $lastWateredTime", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Text(text = "Health: ${plant.healthStatus}/5", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Text(text = "Family: ${plant.family}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Text(text = "Sunlight Needs: ${plant.sunlight}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Text(text = "Water Needs: ${plant.wateringNeeds}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

        var categoryExpanded by remember { mutableStateOf(false) }
        val categories = listOf("Living Room", "Kitchen", "Bedroom", "Unassigned")
        var selectedCategory by remember { mutableStateOf(plant.category.ifEmpty { "Unassigned" }) }

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            TextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false },
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

        var defectExpanded by remember { mutableStateOf(false) }
        var selectedDefect by remember { mutableStateOf(plant.defect) }

        ExposedDropdownMenuBox(
            expanded = defectExpanded,
            onExpandedChange = { defectExpanded = !defectExpanded }
        ) {
            TextField(
                value = selectedDefect.displayName,
                onValueChange = {},
                readOnly = true,
                label = { Text("Defect") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = defectExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = defectExpanded,
                onDismissRequest = { defectExpanded = false },
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

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                onWater(plant)
            },
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
    }
}