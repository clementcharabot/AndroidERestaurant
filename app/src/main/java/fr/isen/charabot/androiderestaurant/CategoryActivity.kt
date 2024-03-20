package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject
import coil.compose.rememberImagePainter

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("category_name") ?: "Catégorie inconnue"

        setContent {
            // Correction ici: Utilisation de mutableStateOf pour créer l'état mutable
            val menuItems = remember { mutableStateOf(listOf<MenuItem>()) }

            AndroidERestaurantTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Correction ici: Utilisation correcte de menuItems.value
                    CategoryScreen(categoryName = categoryName, items = menuItems.value)
                }
            }

            this.fetchMenuItems(categoryName) { items ->
                menuItems.value = items
            }
        }
    }

    private fun fetchMenuItems(categoryName: String, onResult: (List<MenuItem>) -> Unit) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api/catering.bluecodegames.com/menu"
        val params = JSONObject().apply {
            put("id_shop", "1")
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, params, { response ->
            Log.d("CategoryActivity", "Réponse de l'API: $response")
            try {
                val gson = Gson()
                val menuResponse = gson.fromJson(response.toString(), MenuResponse::class.java)
                val filteredItems = menuResponse.data.firstOrNull { it.nameFr == categoryName }?.items ?: emptyList()
                onResult(filteredItems)
            } catch (e: Exception) {
                Log.e("CategoryActivity", "Parsing error", e)
            }
        }, { error ->
            error.printStackTrace()
            Log.e("CategoryActivity", "Volley error: ${error.message}")
            runOnUiThread {
                Toast.makeText(this, "Failed to load data: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })

        queue.add(jsonObjectRequest)
    }
}

@Composable
fun CategoryScreen(categoryName: String, items: List<MenuItem>) {
    Column{
        Text(
            text = categoryName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(items) { item ->
                MenuItemComposable(item)
            }
        }
    }
}

@Composable
fun MenuItemComposable(item: MenuItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (item.images.isNotEmpty()) {
            ImageFromUrls(urls = item
                .images)
        } else {
            Log.d("MenuItemComposable", "Aucune URL d'image pour l'item: ${item.name_fr}")
        }
        Text(text = item.name_fr)
    }
}

@Composable
fun ImageFromUrls(urls: List<String>) {
    var currentUrlIndex by remember { mutableIntStateOf(0) }

    val painter = rememberImagePainter(
        data = urls.getOrNull(currentUrlIndex),
        builder = {
            crossfade(true)
            error(R.drawable.androidcook) // Utilisez une icône d'erreur par défaut d'Android
            listener(
                onError = { _, throwable ->
                    Log.d("ImageFromUrls", "Erreur de chargement pour l'URL : ${urls.getOrNull(currentUrlIndex)} avec l'erreur: ${throwable.message}")
                    if (currentUrlIndex < urls.size - 1) {
                        // Essayez la prochaine URL si celle actuelle échoue
                        currentUrlIndex++
                    }
                }
            )
        }
    )

    Image(
        painter = painter,
        contentDescription = null, // Fournissez une description appropriée pour l'accessibilité.
        modifier = Modifier
            .size(150.dp) // Définissez la taille de l'image. Ajustez selon vos besoins.
            .aspectRatio(1f),
        contentScale = ContentScale.Crop // Gère comment l'image doit être redimensionnée ou déplacée pour remplir les dimensions données.
    )
}





// Classes pour la réponse et les données
data class MenuResponse(
    val data: List<Category> // Assurez-vous que cela correspond au champ "data" du JSON
)

data class Category(
    val nameFr: String, // Nom français de la catégorie
    val items: List<MenuItem> // Liste des items dans cette catégorie
)

data class MenuItem(
    val id: String, // Identifiant de l'item
    val name_fr: String, // Nom français de l'item
    val idCategory : String, // Identifiant de la catégorie de l'item
    val categ_namefr: String, // Nom français de la catégorie de l'item
    val images: List<String>, // URLs des images de l'item
    val ingredients: List<Ingredient>, // Liste des ingrédients de l'item
    val prices: List<Price> // Liste des prix de l'item
)

data class Ingredient(
    val id: String,
    val idShop: String,
    val nameFr: String,
    val createDate: String,
    val updateDate: String,
    val idPizza: String?
)

data class Price(
    val id: String,
    val id_pizza: String,
    val id_size: String,
    val price: String,
    val create_date: String,
    val update_date: String,
    val size: String
)

