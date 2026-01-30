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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import se.mau.grupp7.happyplant2.controller.BackendConnector
import se.mau.grupp7.happyplant2.controller.PlantTypeController
import se.mau.grupp7.happyplant2.model.UserPlant
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
    val plantList = remember { backendConnector?.userPlantController?.GetUserPlants() ?: emptyList() }

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
                GridScreen(
                    plantTypes = plantList,
                    onAddPlant = { /* TODO */ },
                    gotoPlant = { navController.navigate("placeholder") }
                )
            }
            composable("placeholder") { PlaceholderScreen() }
            composable("addPlant") { AddPlantScreen() }
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
        val imageBitmap = ImageBitmap.imageResource(id = R.drawable.bonsai_100)
        Image(
            painter = BitmapPainter(imageBitmap, filterQuality = FilterQuality.None),
            contentDescription = "Bonsai Tree",
            modifier = Modifier.fillMaxSize(0.9f)
        )
    }
}

/**
 * Screen With The Users Plants
 */
@Composable
fun GridScreen(plantTypes: List<UserPlant>, onAddPlant: () -> Unit, gotoPlant: () -> Unit) {
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
            onClick = onAddPlant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Stick to the bottom
        ) {
            Text(text = "Water all Plants")
        }
        Button(
            onClick = gotoPlant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Stick to the bottom
        ) {
            Text(text = "Add Plant")
        }
    }
}

@Composable
fun PlantCard(userPlant: UserPlant) {
    Card(modifier = Modifier.padding(8.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = userPlant.name)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = userPlant.description)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.BottomEnd
        ){
            Button(onClick = { backendConnector?.userPlantController?.WaterUserPlant(userPlant) }) {
                Text(text = "Water")
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
