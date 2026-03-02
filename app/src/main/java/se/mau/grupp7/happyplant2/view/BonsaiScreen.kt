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

                // SHADOW LAYER
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

                // MAIN IMAGE
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
                onDayClick = { date -> selectedDate = date }
            )
        }

        if (selectedDate != null) {
            // Self-note --> Alper:
            // Popup kommer att implementeras här
        }
    }
}

@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    userPlants: List<UserPlant>,
    onDayClick: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    val dates = (0..30).map {
        val newDate = calendar.clone() as Calendar
        newDate.add(Calendar.DAY_OF_YEAR, it)
        newDate.time
    }

    val dayMs = 24L * 60L * 60L * 1000L

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates) { date ->
            val anyPlantNeedsWater = userPlants.any { plant ->
                if (plant.wateringIntervalMin <= 0) return@any false
                val nextWaterTime = plant.lastTimeWatered.time + plant.wateringIntervalMin.toLong() * dayMs
                val startOfDate = Calendar.getInstance().apply {
                    time = date
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                val endOfDate = startOfDate + dayMs
                nextWaterTime in startOfDate until endOfDate
            }

            DayItem(
                date = date,
                needsWatering = anyPlantNeedsWater,
                onClick = { onDayClick(date) }
            )
        }
    }
}

@Composable
fun DayItem(date: Date, needsWatering: Boolean, onClick: () -> Unit = {}) {
    val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
    val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                onClick()
            }
            .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(dayFormat.format(date))
        Text(dateFormat.format(date))
        if (needsWatering) {
            Icon(
                Icons.Filled.WaterDrop,
                contentDescription = null,
                tint = Color.Blue,
                modifier = Modifier.size(16.dp).padding(top = 4.dp)
            )
        } else {
            Spacer(Modifier.size(16.dp).padding(top = 4.dp))
        }
    }
}