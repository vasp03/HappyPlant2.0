package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.model.SortOption
import se.mau.grupp7.happyplant2.model.UserPlant

@Composable
fun LibraryScreen(
    userPlantList: List<UserPlant>,
    navController: NavHostController
) {
    var sortOption by remember { mutableStateOf(SortOption.CommonNameAZ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SortDropdown(
            sortOption = sortOption,
            onSortOptionSelected = { sortOption = it }
        )

        val sortedList = when (sortOption) {
            SortOption.CommonNameAZ -> userPlantList.sortedBy { it.name }
            SortOption.CommonNameZA -> userPlantList.sortedByDescending { it.name }
            SortOption.DateAddedNewest -> userPlantList.sortedByDescending { it.dateAdded }
            SortOption.DateAddedOldest -> userPlantList.sortedBy { it.dateAdded }
            SortOption.NeedOfWaterMost -> userPlantList.sortedBy { it.lastTimeWatered }
            SortOption.NeedOfWaterLeast -> userPlantList.sortedByDescending { it.lastTimeWatered }
        }

        val unassignedPlants = sortedList.filter { it.category.isEmpty() }
        val categorizedPlants = sortedList
            .filter { it.category.isNotEmpty() }
            .groupBy { it.category }

        val displayCategories = categorizedPlants.keys.sorted()

        var expandedCategories by remember(displayCategories) { mutableStateOf(displayCategories.toSet()) }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            displayCategories.forEach { category ->
                val plantsForCategory = categorizedPlants[category] ?: emptyList()

                item {
                    val isExpanded = category in expandedCategories
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .background(Color(0xFF2C2A4A), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedCategories = if (isExpanded) {
                                        expandedCategories - category
                                    } else {
                                        expandedCategories + category
                                    }
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$category (${plantsForCategory.size})",
                                color = Color.White,
                                fontStyle = FontStyle.Italic,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = Color.White
                            )
                        }

                        if (isExpanded) {
                            val chunkedPlants = plantsForCategory.chunked(3)
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                chunkedPlants.forEach { rowPlants ->
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    ) {
                                        rowPlants.forEach { plant ->
                                            UserPlantCard(plant, navController)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (unassignedPlants.isNotEmpty()) {
                item {
                    val chunkedPlants = unassignedPlants.chunked(3)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        chunkedPlants.forEach { rowPlants ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                rowPlants.forEach { plant ->
                                    UserPlantCard(plant, navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropdown(sortOption: SortOption, onSortOptionSelected: (SortOption) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = sortOption.displayName,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            SortOption.entries.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        expanded = false
                        onSortOptionSelected(option)
                    }
                )
            }
        }
    }
}

@Composable
fun UserPlantCard(userPlant: UserPlant, navController: NavHostController) {
    IconButton(
        onClick = { navController.navigate("plantDetails/${userPlant.id}") },
        modifier = Modifier
            .size(90.dp)
            .padding(8.dp)
    ) {
        AsyncImage(
            model = userPlant.imageURL,
            contentDescription = userPlant.name,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}
