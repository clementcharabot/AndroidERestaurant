package fr.isen.charabot.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                HomeScreen(context = context)
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HomeActivity", "L'activité Home est détruite.")
    }
}


@Composable
fun HomeScreen(context: Context) {
    Column {
        val categories = listOf("Entrées", "Plats", "Desserts")
        Text(
            "Bienvenue chez DroidRestaurant",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        )
        // Affichage des catégories
        categories.forEach { category ->
            CategoryCard(name = category, onClick = { categoryName ->
                val intent = Intent(context, CategoryActivity::class.java).apply {
                    putExtra("categoryName", categoryName)
                }
                context.startActivity(intent)
            })
        }
    }
}

    @Composable
    fun CategoryCard(name: String, onClick: (String) -> Unit) {
        Text(
            text = name,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(name) }
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
