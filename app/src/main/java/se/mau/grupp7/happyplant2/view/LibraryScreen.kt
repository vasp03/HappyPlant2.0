package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.R
import se.mau.grupp7.happyplant2.model.SortOption
import se.mau.grupp7.happyplant2.model.UserPlant
import android.content.ClipData
import android.content.ClipDescription
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.SideEffect
import androidx.compose.foundation.lazy.grid.items
import java.util.Date
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.draw.shadow

private val gridSpacing = 16.dp

private const val DAY_MS = 24L * 60L * 60L * 1000L


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LibraryScreen(
    userPlantList: List<UserPlant>,
    navController: NavHostController,
    onNavigateToDiscover: () -> Unit,
    onUpdateCategory: (UserPlant, String) -> Unit,
    onWaterSelected: (List<String>) -> Unit
) {
    var sortOption by remember { mutableStateOf(SortOption.CommonNameAZ) }

    var isWaterSelectMode by remember { mutableStateOf(false) }
    var selectedPlantIds by remember { mutableStateOf(setOf<String>()) } // dina id är String

    val currentUserPlants = remember { mutableStateOf(userPlantList) }

    // Update the mutable state on every recomposition so its always current.
    SideEffect {
        currentUserPlants.value = userPlantList
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

            val categoryMap = sortedList.groupBy { if (it.category.isEmpty()) "Unassigned" else it.category }
            val displayCategories = (categoryMap.keys + "Unassigned").distinct().sortedBy { if (it == "Unassigned") "\uffff" else it }  // Puts "Unassigned" at the end

            var expandedCategories by remember(displayCategories) { mutableStateOf(displayCategories.toSet()) }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                items(
                    items = displayCategories,
                    key = { it }
                ) { category ->
                    val plantsForCategory = categoryMap.getOrDefault(category, emptyList())
                    val isExpanded = category in expandedCategories

                    var isDragOver by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .background(if (isDragOver) Color(0xFF4CAF50).copy(alpha = 0.3f) else Color(0xFF2C2A4A), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .dragAndDropTarget(
                                shouldStartDragAndDrop = { event ->
                                    !isWaterSelectMode && event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                                },
                                target = remember {
                                    object : DragAndDropTarget {
                                        override fun onEntered(event: DragAndDropEvent) {
                                            isDragOver = true
                                        }

                                        override fun onExited(event: DragAndDropEvent) {
                                            isDragOver = false
                                        }

                                        override fun onDrop(event: DragAndDropEvent): Boolean {
                                            isDragOver = false
                                            val clipData = event.toAndroidDragEvent().clipData
                                            val idStr = clipData?.getItemAt(0)?.text?.toString() ?: return false
                                            val plant = currentUserPlants.value.find { it.id == idStr } ?: return false
                                            val newCategory = if (category == "Unassigned") "" else category
                                            if (plant.category != newCategory) {
                                                onUpdateCategory(plant, newCategory)
                                            }
                                            return true
                                        }

                                        override fun onEnded(event: DragAndDropEvent) {
                                            isDragOver = false
                                        }
                                    }
                                }
                            )
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
                            LazyVerticalGrid( //bad implementation, this grid should contain the whole page.
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(gridSpacing),
                                verticalArrangement = Arrangement.spacedBy(gridSpacing),
                                userScrollEnabled = false, //forces bad impl to work
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 10000.dp) //forces bad impl to work
                                    .padding(all = 16.dp)                            ) {
                                items(plantsForCategory, key = { it.id }) { plant ->
                                    val isSelected = selectedPlantIds.contains(plant.id)
                                    UserPlantCard(
                                        userPlant = plant,
                                        navController = navController,
                                        isSelectionMode = isWaterSelectMode,
                                        isSelected = isSelected,
                                        onToggleSelected = { id ->
                                            selectedPlantIds =
                                                if (selectedPlantIds.contains(id)) selectedPlantIds - id
                                                else selectedPlantIds + id
                                        },
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {val selectedCount = selectedPlantIds.size

// 1) VATTNA (commit) – syns bara i selection mode och när man valt minst 1
            if (isWaterSelectMode && selectedCount > 0) {
                FloatingActionButton(
                    onClick = {
                        onWaterSelected(selectedPlantIds.toList())   // DB-uppdatering triggas här och bara här
                        isWaterSelectMode = false
                        selectedPlantIds = emptySet()
                    },
                    containerColor = Color(0xFF4CAF50),
                    shape = CircleShape
                ) {
                    Text("Water ($selectedCount)")
                }
            }

// 2) CANCEL – syns bara i selection mode
            if (isWaterSelectMode) {
                FloatingActionButton(
                    onClick = {
                        isWaterSelectMode = false
                        selectedPlantIds = emptySet()
                    },
                    containerColor = Color(0xFFE57373),
                    shape = CircleShape
                ) {
                    Text("Cancel")
                }
            }

// 3) MODE-knapp – alltid synlig, togglar bara läget (ingen vattning)
            FloatingActionButton(
                onClick = {
                    isWaterSelectMode = !isWaterSelectMode
                    selectedPlantIds = emptySet()
                },
                containerColor = Color(0xFF3A8DFF),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = "Vattningsläge"
                )
            }
            // 4) Add plant – din befintliga navigation till Discover
            FloatingActionButton(
                onClick = { onNavigateToDiscover() },
                containerColor = Color(0xFF4CAF50),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add plant"
                )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserPlantCard(
    userPlant: UserPlant,
    navController: NavHostController,
    isSelectionMode: Boolean,
    isSelected: Boolean,
    onToggleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val needsWater = needsWatering(userPlant.lastTimeWatered, userPlant.wateringInterval)

    Box(modifier = modifier) {

        IconButton(
            onClick = {
                if (isSelectionMode) onToggleSelected(userPlant.id)
                else navController.navigate("plantDetails/${userPlant.id}")
            },
            modifier = modifier
                .dragAndDropSource(block = {
                    if (!isSelectionMode) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                startTransfer(
                                    DragAndDropTransferData(
                                        ClipData.newPlainText("plant", userPlant.id)
                                    )
                                )
                            },
                            onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                change.consume()
                            },
                            onDragCancel = {},
                            onDragEnd = {}
                        )
                    }
                })
        ) {
            AsyncImage(
                model = userPlant.localImageUri ?: userPlant.imageURL.takeIf { it.isNotBlank() },
                contentDescription = userPlant.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.plant_placeholder),
                error = painterResource(R.drawable.plant_placeholder),
                fallback = painterResource(R.drawable.plant_placeholder)
            )
        }

        if (needsWater) {
            WaterDropOverlayIcon(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
            )
        }

        // Markeringsindikator
        if (isSelectionMode) {
            SelectionBadge(
                selected = isSelected,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(6.dp)
            )
        }
    }
}

@Composable
private fun WaterDropOverlayIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Filled.WaterDrop,
        contentDescription = "Needs watering",
        tint = Color(0xFF42A5F5),
        modifier = Modifier
            .size(36.dp)
            .shadow(2.dp, CircleShape, clip = false)
    )
}

fun needsWatering(lastTimeWatered: Date, wateringIntervalDays: Int): Boolean {
    if (wateringIntervalDays <= 0) return false
    val nextWaterTime = lastTimeWatered.time + wateringIntervalDays * DAY_MS
    return System.currentTimeMillis() >= nextWaterTime
}

@Composable
private fun SelectionBadge(selected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(if (selected) Color(0xFF4CAF50) else Color.White.copy(alpha = 0.85f))
            .shadow(2.dp, CircleShape)
    )
}