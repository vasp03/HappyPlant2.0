package se.mau.grupp7.happyplant2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Yard
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import se.mau.grupp7.happyplant2.view.PlantScreen
import se.mau.grupp7.happyplant2.view.LibraryScreen
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme
import se.mau.grupp7.happyplant2.viewmodel.PlantViewModel
import se.mau.grupp7.happyplant2.view.BonsaiScreen
import se.mau.grupp7.happyplant2.view.DiscoverSearchScreen
import android.os.Build
import android.Manifest
import se.mau.grupp7.happyplant2.notification.NotificationHelper

enum class SearchMode { PLANTS, DIAGNOSES }

class MainActivity : ComponentActivity() {
    private val viewModel: PlantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        NotificationHelper.scheduleWateringReminder(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1001)
        }

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

    val plantList by viewModel.flowerList.collectAsStateWithLifecycle()
    val userPlants by viewModel.userPlants.collectAsStateWithLifecycle()
    val suggestions by viewModel.suggestions.collectAsStateWithLifecycle()
    val diseaseList by viewModel.diseaseList.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
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
                                Icon(item.icon, item.title, tint = Color.White)
                            },
                            label = {
                                Text(item.title, color = Color.White)
                            },
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
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

            composable("mainTabs") {

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->

                    when (page) {
                        0 -> DiscoverSearchScreen(
                            plantTypes = plantList,
                            suggestions = suggestions,
                            diseases = diseaseList,
                            onSearchPlants = { viewModel.getFlowers(it) },
                            onLoadDiseases = { viewModel.getDiseases() },
                            onAdd = { plantDetails, daysAgo ->
                                viewModel.addPlantToUserCollection(plantDetails, daysAgo) {
                                    Toast.makeText(
                                        context,
                                        "Failed to add plant",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            isLoading = isLoading
                        )

                        1 -> BonsaiScreen(viewModel)

                        2 -> LibraryScreen(
                            userPlantList = userPlants,
                            navController = navController,
                            onNavigateToDiscover = {
                                scope.launch { pagerState.animateScrollToPage(0) }
                            },
                            onUpdateCategory = { plant, newCategory ->
                                viewModel.updatePlantCategory(plant, newCategory)
                            },
                            onWaterSelected = { ids ->
                                viewModel.waterSelectedPlants(ids)
                            }
                        )
                    }
                }
            }

            composable("plantDetails/{plantId}") { backStackEntry ->

                val plantId = backStackEntry.arguments?.getString("plantId")

                if (plantId == null) {
                    Text("Plant not found", color = Color.White)
                    return@composable
                }

                val plant by viewModel
                    .getPlantById(plantId)
                    .collectAsStateWithLifecycle(initialValue = null)

                val categories by viewModel.categories.collectAsStateWithLifecycle()

                if (plant != null) {
                    PlantScreen(
                        plant = plant!!,
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
                                p, customName, potType, height, notes
                            )
                        },
                        onImageChange = { plant, uri ->
                            viewModel.updatePlantImage(plant, uri)
                        },
                        categories = categories,
                        onClose = { navController.popBackStack() }
                    )
                } else {
                    Text("Plant not found", color = Color.White)
                }
            }
        }
    }
}

data class NavigationItem(val route: String, val icon: ImageVector, val title: String)