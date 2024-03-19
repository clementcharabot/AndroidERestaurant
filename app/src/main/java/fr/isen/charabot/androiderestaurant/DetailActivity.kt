package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedItem = intent.getStringExtra("selected_item") ?: "Détails inconnus"

        setContent {
            AndroidERestaurantTheme {
                DetailScreen(selectedItem)
            }
        }
    }
}

@Composable
fun DetailScreen(selectedItem: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Détails du plat sélectionné :", modifier = Modifier.padding(bottom = 16.dp))
        Text(text = selectedItem)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidERestaurantTheme {
        DetailScreen("Nom du plat")
    }
}
