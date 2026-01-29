package se.mau.grupp7.happyplant2.view.components

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.tooling.preview.Preview
import se.mau.grupp7.happyplant2.MainActivity
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme

@Composable
fun ButtonGrid(
    context: MainActivity,
    modifier: Modifier = Modifier,
    items: List<String> = (1..6).map { "Item $it" },
    columns: Int = 3,
    onItemClick: (String) -> Unit = {}
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(items) { item ->
            Button(
                onClick = { Toast.makeText(context, "Clicked: $item", Toast.LENGTH_SHORT).show() },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(item)
            }
        }
    }
}
