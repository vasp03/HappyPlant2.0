package se.mau.grupp7.happyplant2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import se.mau.grupp7.happyplant2.controller.BackendConnector
import se.mau.grupp7.happyplant2.view.theme.HappyPlant2Theme
import se.mau.grupp7.happyplant2.view.components.ButtonGrid

private var backendConnector : BackendConnector? = null

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup()

        enableEdgeToEdge()
        setContent {
            HappyPlant2Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ButtonGrid(
                        this@MainActivity,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

fun setup(){
    // Setup is run here.
    // Java classes that connects to backend server is initialize here.
    backendConnector = BackendConnector()

    // Throwing NullPointerException here if setup won't work.
    if (backendConnector == null) {
        throw NullPointerException()
    }

    backendConnector?.setup()
}