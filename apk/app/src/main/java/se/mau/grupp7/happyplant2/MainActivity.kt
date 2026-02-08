package se.mau.grupp7.happyplant2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import android.content.Context
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.mau.grupp7.happyplant2.controller.BackendConnector
import se.mau.grupp7.happyplant2.model.FlowerTypes
import se.mau.grupp7.happyplant2.model.PerenualFlowerInterface
import se.mau.grupp7.happyplant2.model.Plant
import se.mau.grupp7.happyplant2.model.WaterAmount
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme

private var backendConnector: BackendConnector? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HappyPlant2Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val initialPlantList = emptyList<Plant>()
    var plantList by remember { mutableStateOf(initialPlantList) }
    val ctx = LocalContext.current
    var userPlantList by remember { mutableStateOf(initialPlantList) }

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
                    onSearch = { query ->
                        getFlowerTypes(ctx, { fetched ->
                            plantList = fetched
                        }, query)
                    },
                    onAdd = { plant ->
                        userPlantList = userPlantList.plus(plant)
                    }
                )
            }
            composable("plantList") {
                UserPlantListScreen(userPlantList) { plant ->
                    userPlantList = userPlantList.minus(plant)
                }
            }
            composable("addPlant") { AddNewPlantScreen() }
        }
    }
}

@Composable
fun UserPlantListScreen(userPlantList: List<Plant>, onRemove: (plant : Plant) -> Unit) {
    Box(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row() {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 96.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    for (plant in userPlantList) {
                        item { UserPlantCard(plant, onRemove) }
                    }
                }
            }
        }
    }
}

fun getFlowerTypes(context: Context, onResult: (List<Plant>) -> Unit, search : String = ""){
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(PerenualFlowerInterface::class.java)

    if (context is ComponentActivity) {
        context.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val res: Array<FlowerTypes> = api.getFlowerTypes(q = search)

                val mapped = res.map { ft ->
                    Plant(
                        ft.id,
                        ft.common_name,
                        ft.scientific_name.joinToString(", "),
                        ft.genus,
                        ft.family,
                        "",
                        ft.thumbnail,
                        WaterAmount.OFTEN,
                        Date(),
                        0
                    )
                }

                withContext(Dispatchers.Main) {
                    onResult(mapped)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Request failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun AddNewPlantScreen(){
    Text("Hello")
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
            DayItem(date = date, needsWatering = (date.date % 3 == 0)) // Testdata för vattning
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
            Box(modifier = Modifier.size(16.dp).padding(top = 4.dp))
        }
    }
}

@Composable
fun BonsaiScreen() {
    var isCalendarVisible by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        val bgImageBitmap = ImageBitmap.imageResource(id = R.drawable.pixelated_background)
        Image(
            painter = BitmapPainter(bgImageBitmap, filterQuality = FilterQuality.None),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        // Darkness filter for background image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.20f))
        )
        // Darkness background for Bonsai Image
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

            // 2. The Bonsai Image, drawn on top of the background
            val bonsaiImageBitmap = ImageBitmap.imageResource(id = R.drawable.bonsai_100)
            Image(
                painter = BitmapPainter( bonsaiImageBitmap, filterQuality = FilterQuality.None),
                contentDescription = "Bonsai Tree",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
            )
        }

        // Settings Icon
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
                contentDescription = "Calender",
                tint = Color.White,
                modifier = Modifier.size(36.dp),
            )
        }
        // Calender Icon
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
                contentDescription = "Calender",
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

/**
 * Screen for discovering plants
 */
@Composable
fun PlantDiscoverScreen(plantTypes: List<Plant>, onSearch: (String) -> Unit, onAdd: (plant : Plant) -> Unit) {
    Box(){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row() {
                SearchScreen(onSearch)
            }

            Row() {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 96.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    for (plant in plantTypes) {
                        item { PlantCard(plant, onAdd) }
                    }
                }
            }
        }
    }
}

@Composable
fun PlantCard(userPlant: Plant, onAdd: (plant : Plant) -> Unit) {
    var plant = userPlant

    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Thumbnail image
            AsyncImage(
                model = userPlant.imageURL,
                contentDescription = userPlant.common_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            // Name and description
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(text = userPlant.common_name)
                    Text(text = userPlant.scientific_name)
                }
            }

            Button(
                onClick = { onAdd(plant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Plant")
            }
        }
    }
}

@Composable
fun UserPlantCard(userPlant: Plant, onRemove: (plant : Plant) -> Unit) {
    var plant = userPlant

    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Thumbnail image
            AsyncImage(
                model = userPlant.imageURL,
                contentDescription = userPlant.common_name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )

            // Name and description
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(text = userPlant.common_name)
                    Text(text = userPlant.scientific_name)
                }
            }

            Button(
                onClick = { onRemove(plant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Remove Plant")
            }
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HappyPlant2Theme {
        MainScreen()
    }
}
