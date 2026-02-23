package se.mau.grupp7.happyplant2.view

import se.mau.grupp7.happyplant2.R
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.model.Defect
import se.mau.grupp7.happyplant2.model.DefectList
import se.mau.grupp7.happyplant2.model.UserPlant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantScreen(
    plant: UserPlant,
    onRemove: (UserPlant) -> Unit,
    onWater: (UserPlant) -> Unit,
    onCategoryChange: (UserPlant, String) -> Unit,
    onDefectChange: (UserPlant, Defect) -> Unit,
    onDetailsChange: (UserPlant, String, String, String, String) -> Unit,
    onImageChange: (UserPlant, String) -> Unit,
    categories: List<String>,
    onClose: () -> Unit
) {

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val lastWateredTime by remember(plant.lastTimeWatered) {
        mutableStateOf(dateFormat.format(plant.lastTimeWatered))
    }

    var categoryExpanded by remember { mutableStateOf(false) }
    var selectedPlantCategory by remember { mutableStateOf(plant.category.ifEmpty { "" }) }
    var selectedDefectCategory by remember { mutableStateOf<String?>(null) }
    var selectedDefectSubCategory by remember { mutableStateOf<String?>(null) }
    var defectExpanded by remember { mutableStateOf(false) }
    val currentDefect = DefectList.findById(plant.defectId)
    var customName by remember(plant.id) { mutableStateOf(plant.customName.ifEmpty { plant.name }) }
    var potType by remember(plant.id) { mutableStateOf(plant.potType) }
    var heightCm by remember(plant.id) { mutableStateOf(plant.heightCm) }
    var notes by remember(plant.id) { mutableStateOf(plant.notes) }


    val context = LocalContext.current

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onImageChange(plant, it.toString())
        }
    }

    val currentTime = Date()
    val timeSinceLast = currentTime.time - plant.lastTimeWatered.time
    val daysSinceLastFloat = (timeSinceLast / (1000L * 60 * 60 * 24)).toFloat()
    val progress = max(0f, 1f - (daysSinceLastFloat / plant.wateringIntervalMax.toFloat()))
    val dottedFraction = plant.wateringIntervalMin.toFloat() / plant.wateringIntervalMax.toFloat()

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
                model = plant.localImageUri
                    ?: plant.imageURL.takeIf { it.isNotBlank() },
                contentDescription = plant.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clickable {
                        imageLauncher.launch("image/*")
                    },
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.plant_placeholder),
                error = painterResource(R.drawable.plant_placeholder),
                fallback = painterResource(R.drawable.plant_placeholder)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = customName,
                onValueChange = {
                    customName = it
                    onDetailsChange(plant, customName, potType, heightCm, notes)
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plant.description,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(5) { index ->
                    val isGreen = index < plant.healthStatus
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = if (isGreen) Color(37, 204, 0) else Color(204, 38, 0),
                                shape = CircleShape
                            )
                    )
                    if (index < 4) {
                        val nextIsGreen = (index + 1) < plant.healthStatus
                        val lineColor = when {
                            isGreen && nextIsGreen -> Color(37, 204, 0)
                            !isGreen -> Color(204, 38, 0)
                            else -> Color(204, 197, 0)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .background(lineColor)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
            ) {
                Column {
                    Text(
                        text = "Last watered: $lastWateredTime",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    if (plant.family.isNotBlank()) {
                        Text(text = "Family: ${plant.family}", color = Color.White)

                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(text = "Sunlight Needs: ${plant.sunlight}", color = Color.White)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = "Water Needs: ${plant.wateringNeeds}", color = Color.White)
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray)
                        .drawWithContent {
                            drawContent()
                            val lineY = size.height * dottedFraction
                            drawLine(
                                color = Color(0xFFFF6D41),
                                start = Offset(0f, lineY),
                                end = Offset(size.width, lineY),
                                strokeWidth = 10f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(progress)
                            .align(Alignment.BottomStart)
                            .background(Color(0xFF3A8DFF))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                TextField(
                    value = selectedPlantCategory,
                    onValueChange = { selectedPlantCategory = it },
                    label = { Text("Category (type to add new)") },
                    trailingIcon = {
                        Row {
                            IconButton(
                                onClick = {
                                    onCategoryChange(plant, selectedPlantCategory)
                                    categoryExpanded = false
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Save Category"
                                )
                            }
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                        }
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
                                selectedPlantCategory = category
                                categoryExpanded = false
                                onCategoryChange(plant, category)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = defectExpanded,
                onExpandedChange = { defectExpanded = !defectExpanded }
            ) {
                TextField(
                    value = currentDefect.displayName,
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

                    if (selectedDefectCategory == null) {
                        DefectList.categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedDefectCategory = category
                                }
                            )
                        }
                    }

                    else if (selectedDefectSubCategory == null) {
                        DefectList.subCategories(selectedDefectCategory!!)
                            .forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub) },
                                    onClick = {
                                        selectedDefectSubCategory = sub
                                    }
                                )
                            }

                        DropdownMenuItem(
                            text = { Text("← Back") },
                            onClick = { selectedDefectCategory = null }
                        )
                    }

                    else {
                        DefectList
                            .defects(selectedDefectCategory!!, selectedDefectSubCategory!!)
                            .forEach { defect ->

                                DropdownMenuItem(
                                    text = {
                                        Text(defect.displayName)
                                    },
                                    onClick = {
                                        onDefectChange(plant, defect)

                                        selectedDefectCategory = null
                                        selectedDefectSubCategory = null
                                        defectExpanded = false
                                    }
                                )
                            }

                        DropdownMenuItem(
                            text = { Text("← Back") },
                            onClick = { selectedDefectSubCategory = null }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = potType,
                    onValueChange = {
                        potType = it
                        onDetailsChange(plant, customName, potType, heightCm, notes)
                    },
                    label = { Text("Pot Type") },
                    modifier = Modifier.weight(1f)
                )

                TextField(
                    value = heightCm,
                    onValueChange = {
                        heightCm = it
                        onDetailsChange(plant, customName, potType, heightCm, notes)
                    },
                    label = { Text("Height") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = notes,
                onValueChange = {
                    notes = it
                    onDetailsChange(plant, customName, potType, heightCm, notes)
                },
                label = { Text("General Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onRemove(plant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove Plant")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            FloatingActionButton(
                onClick = { onWater(plant) },
                containerColor = Color(0xFF3A8DFF),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = "Water plants"
                )
            }
        }

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