package se.mau.grupp7.happyplant2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.height
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import se.mau.grupp7.happyplant2.controller.BackendConnector
import se.mau.grupp7.happyplant2.controller.PlantTypeController
import se.mau.grupp7.happyplant2.model.FlowerTypes
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme

private var backendConnector: BackendConnector? = null
private var plantTypeController: PlantTypeController? = null

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

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
    val initialPlantList = emptyList<PlantDetails>()
    var plantList by remember { mutableStateOf(initialPlantList) }
    val ctx = LocalContext.current

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color(0xFFF8DEAD)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { BonsaiScreen() }
            composable("plantList") {
                FlowerDiscoverScreen(
                    plantTypes = plantList,
                    getAllPlants = {
                        getFlowerTypes(ctx) { fetched ->
                            plantList = fetched
                        }
                    }
                )
            }
            composable("placeholder") { PlaceholderScreen() }
            composable("addPlant") { AddPlantScreen() }
        }
    }
}

fun getFlowerTypes(context: Context, onResult: (List<PlantDetails>) -> Unit){
    val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(se.mau.grupp7.happyplant2.model.PerenualFlowerInterface::class.java)

    if (context is ComponentActivity) {
        context.lifecycleScope.launch(Dispatchers.IO) {
            try {
                val res: Array<FlowerTypes> = api.getFlowerTypes()

                val mapped = res.map { ft ->
                    PlantDetails(
                        ft.id,
                        ft.common_name,
                        ft.scientific_name.joinToString(", "),
                        ft.genus,
                        ft.regular_url,
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
fun AddPlantScreen(){
    Text("Hello")
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("home", Icons.Default.Home, "Home"),
        NavigationItem("plantList", Icons.AutoMirrored.Filled.List, "Grid"),
        NavigationItem("placeholder", Icons.Default.Settings, "Placeholder")
    )
    NavigationBar(containerColor = Color(0xFFF8DEAD)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val title: String)

@Composable
fun BonsaiScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bonsai_100),
            contentDescription = "Bonsai Tree"
        )
    }
}

/**
 * Screen With The Users Plants
 */
@Composable
fun FlowerDiscoverScreen(plantTypes: List<PlantDetails>, getAllPlants: () -> Unit) {
    Column(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.weight(1f) // Make the grid take up all available space
        ) {
            items(plantTypes) { plantType ->
                PlantCard(plantType)
            }
        }
        Button(
            onClick = getAllPlants,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Stick to the bottom
        ) {
            Text(text = "Get All Plants")
        }
    }
}

@Composable
fun PlantCard(userPlant: PlantDetails) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Thumbnail image
            AsyncImage(
                model = userPlant.imageUrl,
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
        }
    }
}

@Composable
fun PlaceholderScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "No plant has been selected")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HappyPlant2Theme {
        MainScreen()
    }
}


fun setup() {
    // Setup is run here.
    // Java classes that connects to backend server is initialize here.
    backendConnector = BackendConnector()

    // Throwing NullPointerException here if setup won't work.
    if (backendConnector == null) {
        throw NullPointerException()
    }

    backendConnector?.setup()
    plantTypeController = PlantTypeController(backendConnector)
}
