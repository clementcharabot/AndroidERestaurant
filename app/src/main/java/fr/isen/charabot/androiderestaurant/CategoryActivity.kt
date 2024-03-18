package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("category_name") ?: "Catégorie Inconnue"

        setContent {
            AndroidERestaurantTheme {
                CategoryScreen(categoryName)
            }
        }
    }
}

@Composable
fun CategoryScreen(categoryName: String) {
    val context = LocalContext.current
    val items = when (categoryName) {
        context.getString(R.string.entrees) -> stringArrayResource(id = R.array.entrees_list).toList()
        context.getString(R.string.main_courses) -> stringArrayResource(id = R.array.main_courses_list).toList()
        context.getString(R.string.desserts) -> stringArrayResource(id = R.array.desserts_list).toList()
        else -> listOf<String>()
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(items) { item ->
            Text(text = item, modifier = Modifier.padding(vertical = 8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidERestaurantTheme {
        CategoryScreen("Entrée")
    }
}
