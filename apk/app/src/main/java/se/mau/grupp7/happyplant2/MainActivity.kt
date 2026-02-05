package se.mau.grupp7.happyplant2

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import se.mau.grupp7.happyplant2.model.PlantDetails
import se.mau.grupp7.happyplant2.model.UserPlant
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme
import se.mau.grupp7.happyplant2.viewmodel.PlantViewModel

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
        containerColor = Color(0xFFF8DEAD)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { BonsaiScreen() }
            composable("discover") { FlowerDiscoverScreen(
                plantTypes = plantList,
                getAllPlants = { viewModel.getFlowers("rose") },
                onAdd = { plantDetails -> 
                    viewModel.addPlantToUserCollection(plantDetails) {
                        Toast.makeText(context, "Failed to add plant", Toast.LENGTH_SHORT).show()
                    }
                }
            ) }
            composable("myPlants") {
                MyPlantsScreen(
                    userPlants = userPlants,
                    onWater = { plant -> viewModel.waterUserPlant(plant) },
                    onDelete = { plant -> viewModel.deleteUserPlant(plant) }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("home", Icons.Default.Home, "Home"),
        NavigationItem("discover", Icons.AutoMirrored.Filled.List, "Discover"),
        NavigationItem("myPlants", Icons.Default.Settings, "My Plants")
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

@Composable
fun FlowerDiscoverScreen(
    plantTypes: List<PlantDetails>,
    getAllPlants: () -> Unit,
    onAdd: (PlantDetails) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(plantTypes) { plantType ->
                PlantCard(plantType, onAdd = { onAdd(plantType) })
            }
        }
        Button(
            onClick = getAllPlants,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text = "Get Plants (Rose)")
        }
    }
}

@Composable
fun PlantCard(
    plantDetails: PlantDetails,
    onAdd: () -> Unit
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = plantDetails.imageUrl,
                contentDescription = plantDetails.common_name,
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(text = plantDetails.common_name)
                    Text(text = plantDetails.scientific_name)
                    Button(onClick = onAdd) {
                        Text("Add to My Plants")
                    }
                }
            }
        }
    }
}

@Composable
fun MyPlantsScreen(
    userPlants: List<UserPlant>,
    onWater: (UserPlant) -> Unit,
    onDelete: (UserPlant) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(userPlants) { plant ->
            UserPlantCard(
                plant = plant,
                onWater = onWater,
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun UserPlantCard(
    plant: UserPlant,
    onWater: (UserPlant) -> Unit,
    onDelete: (UserPlant) -> Unit
) {
    Card(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = plant.imageURL,
                contentDescription = plant.name,
                modifier = Modifier.fillMaxWidth().height(140.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    Text(text = plant.name)
                    Text(text = plant.description)
                    Text(text = "Needs water in ${plant.daysUntilWatering()} days")
                    Button(onClick = { onWater(plant) }) {
                        Text("Water Now")
                    }
                    Button(onClick = { onDelete(plant) }) {
                        Text("Delete")
                    }
                }
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
