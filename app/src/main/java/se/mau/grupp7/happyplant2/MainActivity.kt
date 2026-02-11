package se.mau.grupp7.happyplant2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.SortOption
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme
import se.mau.grupp7.happyplant2.viewmodel.PlantViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val viewModel: PlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappyPlant2Theme {
                MainScreen(viewModel)
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: PlantViewModel) {
    val navController = rememberNavController()
    val plantList by viewModel.flowerList.collectAsState()
    val userPlants by viewModel.userPlants.collectAsState()
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color(0xFF23213E)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { BonsaiScreen() }
            composable("search") { 
                PlantDiscoverScreen(
                    plantTypes = plantList,
                    onSearch = { query -> viewModel.getFlowers(query) },
                    onAdd = { plantDetails -> 
                        viewModel.addPlantToUserCollection(plantDetails) {
                            Toast.makeText(context, "Failed to add plant", Toast.LENGTH_SHORT).show()
                        }
                    }
                ) 
            }
            composable("plantList") {
                UserPlantListScreen(
                    userPlantList = userPlants,
                    navController = navController
                )
            }
            composable("plantDetails/{plantId}") { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId")
                val plant = userPlants.find { it.id == plantId }
                if (plant != null) {
                    PlantDetailsScreen(
                        plant = plant,
                        onRemove = {
                            viewModel.deleteUserPlant(it)
                            navController.popBackStack()
                        },
                        onWater = { viewModel.waterUserPlant(it) },
                        onCategoryChange = { p, cat -> viewModel.updatePlantCategory(p, cat) }
                    )
                } else {
                    Text("Plant not found", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("search", Icons.AutoMirrored.Filled.MenuBook, "Search"),
        NavigationItem("home", Icons.Filled.Forest, "Home"),
        NavigationItem("plantList", Icons.Filled.Yard, "Your Plants")
    )
    NavigationBar(containerColor = Color(0xFF23213E)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title, tint = Color.White) },
                label = { Text(text = item.title, color = Color.White) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0xFF1A1830)
                ),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val title: String)

@Composable
fun CalendarView(modifier: Modifier = Modifier) {
    val calendar = Calendar.getInstance()
    val dates = (0..30).map {
        val newDate = calendar.clone() as Calendar
        newDate.add(Calendar.DAY_OF_YEAR, it)
        newDate.time
    }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates) { date ->
            val cal = Calendar.getInstance()
            cal.time = date
            DayItem(date = date, needsWatering = (cal.get(Calendar.DAY_OF_MONTH) % 3 == 0))
        }
    }
}

@Composable
fun DayItem(date: Date, needsWatering: Boolean) {
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(text = dayFormat.format(date))
        Text(text = dateFormat.format(date))
        if (needsWatering) {
            Icon(
                Icons.Filled.WaterDrop,
                contentDescription = "Needs watering",
                tint = Color.Blue,
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun BonsaiScreen() {
    var isCalendarVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        val bgImageBitmap = ImageBitmap.imageResource(id = R.drawable.pixelated_background)
        Image(
            painter = BitmapPainter(bgImageBitmap, filterQuality = FilterQuality.None),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.20f))
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(530.dp)
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.50f),
                        shape = RoundedCornerShape(20.dp)
                    )
            )

            val bonsaiImageBitmap = ImageBitmap.imageResource(id = R.drawable.bonsai_100)
            Image(
                painter = BitmapPainter(bonsaiImageBitmap, filterQuality = FilterQuality.None),
                contentDescription = "Bonsai Tree",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
            )
        }

        IconButton(
            onClick = { /* TODO: Navigate to settings */ },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .size(48.dp)
                .background(Color(Color.Black.copy(alpha = 0.30f).value), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier.size(36.dp),
            )
        }
        IconButton(
            onClick = { isCalendarVisible = !isCalendarVisible },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp)
                .size(48.dp)
                .background(Color(Color.Black.copy(alpha = 0.30f).value), shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Filled.DateRange,
                contentDescription = "Calendar",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }

        if (isCalendarVisible) {
            CalendarView(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 80.dp)
            )
        }
    }
}

@Composable
fun PlantDiscoverScreen(
    plantTypes: List<PlantDetails>,
    onSearch: (String) -> Unit,
    onAdd: (PlantDetails) -> Unit
) {
    var sortOption by remember { mutableStateOf(SortOption.CommonNameAZ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchScreen(onSearch)
        SortDropdown(
            sortOption = sortOption,
            onSortOptionSelected = { sortOption = it }
        )

        val sortedList = when (sortOption) {
            SortOption.CommonNameAZ -> plantTypes.sortedBy { it.common_name }
            SortOption.CommonNameZA -> plantTypes.sortedByDescending { it.common_name }
            else -> plantTypes.sortedBy { it.common_name }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start = 8.dp,
                top = 8.dp,
                end = 8.dp,
                bottom = 96.dp
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            items(sortedList) { plant ->
                PlantCard(plant, onAdd)
            }
        }
    }
}

@Composable
fun UserPlantListScreen(
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

        val displayCategories = (categorizedPlants.keys + listOf("Living Room", "Kitchen", "Bedroom")).distinct().sorted()

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
fun PlantCard(plantDetails: PlantDetails, onAdd: (PlantDetails) -> Unit) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = plantDetails.imageUrl,
                contentDescription = plantDetails.common_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(text = plantDetails.common_name)
                    Text(text = plantDetails.scientific_name)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDetailsScreen(
    plant: UserPlant,
    onRemove: (UserPlant) -> Unit,
    onWater: (UserPlant) -> Unit,
    onCategoryChange: (UserPlant, String) -> Unit
) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    var lastWateredTime by remember(plant.lastTimeWatered) { mutableStateOf(dateFormat.format(plant.lastTimeWatered)) }
    
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
        Spacer(modifier = Modifier.height(16.dp))

        var expanded by remember { mutableStateOf(false) }
        val categories = listOf("Living Room", "Kitchen", "Bedroom", "Unassigned")
        var selectedCategory by remember { mutableStateOf(plant.category.ifEmpty { "Unassigned" }) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedCategory,
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
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            selectedCategory = category
                            expanded = false
                            onCategoryChange(plant, category)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onSearch: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { onSearch(text) }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search Icon")
                }
            },
            shape = RoundedCornerShape(8.dp),
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HappyPlant2Theme {
        MainScreen(PlantViewModel())
    }
}
