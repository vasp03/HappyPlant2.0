package se.mau.grupp7.happyplant2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.model.Defect
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.view.PlantScreen
import se.mau.grupp7.happyplant2.view.LibraryScreen
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
    val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 })
    val scope = rememberCoroutineScope()

    val plantList by viewModel.flowerList.collectAsState()
    val userPlants by viewModel.userPlants.collectAsState()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar on details screen
    val showBottomBar = currentRoute != "plantDetails/{plantId}"

    Scaffold(
        containerColor = Color(0xFF23213E),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color(0xFF23213E)) {

                    val items = listOf(
                        NavigationItem("plantSearch", Icons.AutoMirrored.Filled.MenuBook, "PlantSearch"),
                        NavigationItem("home", Icons.Filled.Forest, "Home"),
                        NavigationItem("plantList", Icons.Filled.Yard, "Your Plants")
                    )

                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    item.icon,
                                    contentDescription = item.title,
                                    tint = Color.White
                                )
                            },
                            label = {
                                Text(text = item.title, color = Color.White)
                            },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFF1A1830)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "mainTabs",
            modifier = Modifier.padding(innerPadding)
        ) {

            // Main tab container (swipeable)
            composable("mainTabs") {

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->

                    when (page) {

                        0 -> {
                            PlantDiscoverScreen(
                                plantTypes = plantList,
                                onSearch = { query ->
                                    viewModel.getFlowers(query)
                                },
                                onAdd = { plantDetails ->
                                    viewModel.addPlantToUserCollection(plantDetails) {
                                        Toast.makeText(
                                            context,
                                            "Failed to add plant",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            )
                        }

                        1 -> {
                            BonsaiScreen(viewModel)
                        }

                        2 -> {
                            LibraryScreen(
                                userPlantList = userPlants,
                                navController = navController
                            )
                        }
                    }
                }
            }

            // Details screen (separate navigation destination)
            composable("plantDetails/{plantId}") { backStackEntry ->

                val plantId = backStackEntry.arguments?.getString("plantId")
                val plant = userPlants.find { it.id == plantId }
                val categories by viewModel.categories.collectAsState()

                if (plant != null) {
                    PlantScreen(
                        plant = plant,

                        onRemove = {
                            viewModel.deleteUserPlant(it)
                            navController.popBackStack()
                        },

                        onWater = { viewModel.waterUserPlant(it) },

                        onCategoryChange = { p, cat ->
                            viewModel.updatePlantCategory(p, cat)
                        },

                        onDefectChange = { p, defect ->
                            viewModel.updatePlantDefect(p, defect)
                        },

                        onDetailsChange = { p, customName, potType, height, notes ->
                            viewModel.updatePlantDetails(
                                p,
                                customName,
                                potType,
                                height,
                                notes
                            )
                        },

                        onImageChange = { plant, uri ->
                            viewModel.updatePlantImage(plant, uri)
                        },

                        defectOptions = Defect.entries,

                        categories = categories,

                        onClose = {
                            navController.popBackStack()
                        }
                    )

                } else {
                    Text("Plant not found", color = Color.White)
                }
            }
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
fun BonsaiScreen(viewModel: PlantViewModel) {
    var isCalendarVisible by remember { mutableStateOf(false) }
    val healthPercentage by viewModel.overallHealthPercentage.collectAsState()

    val bonsaiRes = when {
        healthPercentage >= 90 -> R.drawable.bonsai_100
        healthPercentage >= 70 -> R.drawable.bonsai_80
        healthPercentage >= 50 -> R.drawable.bonsai_60
        healthPercentage >= 30 -> R.drawable.bonsai_40
        healthPercentage >= 10 -> R.drawable.bonsai_20
        else -> R.drawable.bonsai_0
    }

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

            val bonsaiImageBitmap = ImageBitmap.imageResource(id = bonsaiRes)
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
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Search for plants") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            trailingIcon = {
                IconButton(onClick = { onSearch(text) }) {
                    Icon(Icons.Filled.Search, contentDescription = "Search")
                }
            }
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(plantTypes) { plantType ->
                PlantCard(plantType, onAdd = { onAdd(plantType) })
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

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HappyPlant2Theme {
        MainScreen(PlantViewModel())
    }
}
