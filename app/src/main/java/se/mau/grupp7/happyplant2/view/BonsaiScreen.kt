package se.mau.grupp7.happyplant2.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import se.mau.grupp7.happyplant2.viewmodel.PlantViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import se.mau.grupp7.happyplant2.R
import se.mau.grupp7.happyplant2.model.UserPlant
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import se.mau.grupp7.happyplant2.view.theme.PurpleGrey40

private const val DAY_MS = 24L * 60L * 60L * 1000L

@Composable
fun BonsaiScreen(viewModel: PlantViewModel) {
    var isCalendarVisible by remember { mutableStateOf(false) }
    val healthPercentage by viewModel.overallHealthPercentage.collectAsStateWithLifecycle()
    val userPlants by viewModel.userPlants.collectAsStateWithLifecycle()
    var selectedDate by remember { mutableStateOf<Date?>(null) }

    val bonsaiRes = when {
        healthPercentage >= 90 -> R.drawable.bonsai_100_ai
        healthPercentage >= 70 -> R.drawable.bonsai_80_ai
        healthPercentage >= 50 -> R.drawable.bonsai_60_ai
        healthPercentage >= 30 -> R.drawable.bonsai_40_ai
        healthPercentage >= 10 -> R.drawable.bonsai_20_ai
        else -> R.drawable.bonsai_0_ai
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val bgImageBitmap = ImageBitmap.imageResource(id = R.drawable.pixelated_background)
        Image(
            painter = BitmapPainter(bgImageBitmap, filterQuality = FilterQuality.None),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {

            val shiftAmount = maxHeight * 0.02f

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {

                Image(
                    painter = BitmapPainter(
                        ImageBitmap.imageResource(id = bonsaiRes),
                        filterQuality = FilterQuality.None
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    colorFilter = ColorFilter.tint(
                        Color.Black,
                        blendMode = BlendMode.SrcIn
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.25f)
                        .offset(y = -shiftAmount)
                        .alpha(0.9f)
                        .blur(24.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                        .graphicsLayer(clip = false)
                )

                Image(
                    painter = BitmapPainter(
                        ImageBitmap.imageResource(id = bonsaiRes),
                        filterQuality = FilterQuality.None
                    ),
                    contentDescription = "Bonsai Tree",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.12f)
                        .offset(y = -shiftAmount)
                )
            }
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
                    .padding(top = 80.dp),
                userPlants = userPlants,
                selectedDate = selectedDate,
                onDayClick = { date -> selectedDate = date }
            )
        }

        if (selectedDate != null) {
            DayPopup(
                selectedDate = selectedDate!!,
                userPlants = userPlants,
                onDismiss = { selectedDate = null }
            )
        }
    }
}

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    userPlants: List<UserPlant>,
    selectedDate: Date?,
    onDayClick: (Date) -> Unit
) {
    val dates = remember {
        val calendar = Calendar.getInstance()
        (0..30).map {
            val newDate = calendar.clone() as Calendar
            newDate.add(Calendar.DAY_OF_YEAR, it)
            newDate.time
        }
    }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates) { date ->
            val anyPlantNeedsWater = plantsNeedingWaterOn(date, userPlants).isNotEmpty()

            DayItem(
                date = date,
                needsWatering = anyPlantNeedsWater,
                isSelected = isSameDay(date, selectedDate),
                onClick = { onDayClick(date) }
            )
        }
    }
}

@Composable
fun DayItem(date: Date, needsWatering: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    val dayFormat = remember {SimpleDateFormat("EEE", Locale.getDefault())}
    val dateFormat = remember {SimpleDateFormat("dd", Locale.getDefault())}

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(45.dp)
            .clickable {
                onClick()
            }
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                else Color.White.copy(alpha = 0.8f),
                RoundedCornerShape(8.dp)
            )
    ) {
        Text(dayFormat.format(date), color = if (isSelected) Color.White else Color.Black)
        Text(dateFormat.format(date), color = if (isSelected) Color.White else Color.Black)
        if (needsWatering) {
            Icon(
                Icons.Filled.WaterDrop,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier
                    .size(16.dp)
                    .padding(top = 4.dp)
            )
        } else {
            Spacer(Modifier
                .size(16.dp)
                .padding(top = 4.dp))
        }
    }
}

@Composable
fun DayPopup(
    selectedDate: Date,
    userPlants: List<UserPlant>,
    onDismiss: () -> Unit
) {
    var showUpcoming by remember { mutableStateOf(false) }

    val todayPlants = remember(selectedDate, userPlants) {
        plantsNeedingWaterOn(selectedDate, userPlants)
    }

    val upcomingPlants = remember(selectedDate, userPlants) {
        val result = mutableListOf<Pair<Date, UserPlant>>()
        for (i in 1..29) {
            val futureDate = Date(selectedDate.time + i * DAY_MS)
            val plants = plantsNeedingWaterOn(futureDate, userPlants)
            plants.forEach { plant ->
                result.add(futureDate to plant)
            }
        }
        result
    }

    val dateFormat = remember {SimpleDateFormat("EEE dd MMM", Locale.getDefault())}

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                text = dateFormat.format(selectedDate),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showUpcoming = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!showUpcoming) MaterialTheme.colorScheme.primary else PurpleGrey40),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Dagens", fontSize = 12.sp)
                    }
                    Button(
                        onClick = { showUpcoming = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (showUpcoming) MaterialTheme.colorScheme.primary else PurpleGrey40),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kommande", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!showUpcoming) {
                    if (todayPlants.isEmpty()) {
                        Text("Inga växter behöver vattnas denna dag.", color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        todayPlants.forEach { plant ->
                            Text(
                                text = "💧 ${plant.customName.ifEmpty { plant.name }}",
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                } else {
                    if (upcomingPlants.isEmpty()) {
                        Text("Inga bevattningar de kommande 29 dagarna.", color = MaterialTheme.colorScheme.onSurface)
                    } else {
                        LazyColumn(
                            modifier = Modifier.heightIn(max = 300.dp)
                        ) {
                            items(upcomingPlants) { (date, plant) ->
                                Text(
                                    text = "💧 ${dateFormat.format(date)} — ${plant.customName.ifEmpty { plant.name }}",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Stäng", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

private fun plantsNeedingWaterOn(
    date: Date,
    userPlants: List<UserPlant>
): List<UserPlant> {

    val startOfDay = Calendar.getInstance().apply {
        time = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    return userPlants.filter { plant ->
        if (plant.wateringIntervalMin <= 0) return@filter false

        val lastWateredDay = Calendar.getInstance().apply {
            time = plant.lastTimeWatered
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val daysSinceWatered = ((startOfDay - lastWateredDay) / DAY_MS).toInt()

        if (daysSinceWatered <= 0) return@filter false

        daysSinceWatered % plant.wateringIntervalMin == 0
    }
}

private fun isSameDay(date1: Date, date2: Date?): Boolean {
    if (date2 == null) return false
    val cal1 = Calendar.getInstance().apply { time = date1 }
    val cal2 = Calendar.getInstance().apply { time = date2 }
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}