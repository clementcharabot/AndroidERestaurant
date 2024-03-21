package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("categoryName") ?: "Catégorie"

        setContent {
            val menuItems = remember {
                mutableStateOf<List<MenuItem>>(listOf())
            }

            AndroidERestaurantTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                        MenuScreen(categoryName = categoryName, items = menuItems.value)
                }
            }
            fetchMenuItems(categoryName) { items ->
                menuItems.value = items
            }
        }
    }

    private fun fetchMenuItems(categoryName: String, onResult: (List<MenuItem>) -> Unit) {
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"
        val params = JSONObject()
        params.put("id_shop", "1")

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, params,
            { response ->
                Log.d("CategoryActivity", "Réponse de l'API: $response") // Ajout du log ici
                try {
                    val gson = Gson()
                    val menuResponse = gson.fromJson(response.toString(), MenuResponse::class.java)
                    val filteredItems =
                        menuResponse.data.firstOrNull { it.name_fr == categoryName }?.items
                            ?: emptyList()
                    Log.d("CategoryActivity", "Éléments filtrés pour '$categoryName': $filteredItems")
                    onResult(filteredItems)
                } catch (e: Exception) {
                    Log.e("CategoryActivity", "Parsing error", e)
                }
            },
            { error ->
                error.printStackTrace()
                val errorMessage = error.message ?: "Unknown error" // Utilisez le message d'erreur ou "Unknown error" s'il est nul
                Log.e("CategoryActivity", "Volley error: $errorMessage")
                this@CategoryActivity.runOnUiThread { // Utilisez this@CategoryActivity pour obtenir le contexte de l'activité
                    Toast.makeText(this@CategoryActivity, "Failed to load data: $errorMessage", Toast.LENGTH_LONG)
                        .show()
                }
            })

        queue.add(jsonObjectRequest)
    }
}

@Composable
fun MenuScreen(categoryName: String, items: List<MenuItem>) {
    Column {
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
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally // Centre les éléments horizontalement
    ) {
        val imageUrl = item.images.firstOrNull()
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .build()
        )

        // Réduire la taille de l'image pour qu'elle soit plus petite
        Image(
            painter = painter,
            contentDescription = "Image de ${item.name_fr}",
            modifier = Modifier
                .size(120.dp) // Taille spécifique pour les images
                .aspectRatio(1f), // Maintient un aspect ratio carré
            contentScale = ContentScale.Crop
        )

        // S'il y a une erreur de chargement de l'image, afficher un message d'erreur.
        if (painter.state is AsyncImagePainter.State.Error) {
            Text(
                text = "Impossible de charger l'image",
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Titre du plat
        Text(
            text = item.name_fr,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Prix du plat
        Text(
            text = getFormattedPrice(item.prices),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}


fun getFormattedPrice(prices: List<Price>): String {
    return if (prices.isNotEmpty()) {
        "${prices[0].price}€" // Vous pouvez ajouter votre propre logique de formatage ici
    } else "Prix non disponible"
}
@Composable
fun ImageFromUrls(urls: List<String>) {
    var currentUrlIndex by remember { mutableStateOf(0) }

    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current).data(urls.getOrNull(currentUrlIndex)).build()
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
    val name_fr: String, // Nom français de la catégorie
    val items: List<MenuItem> // Liste des items dans cette catégorie
)

data class Ingredient(
    val id: String, // Identifiant de l'ingrédient
    val id_shop: String, // Identifiant du magasin/shop
    val name_fr: String, // Nom français de l'ingrédient
    val create_date: String, // Date de création de l'ingrédient
    val update_date: String, // Date de mise à jour de l'ingrédient
    val id_pizza: String? // Identifiant de la pizza (si applicable, peut ne pas être présent pour tous les ingrédients, donc nullable)
)

data class Price(
    val id: String, // Identifiant du prix
    val id_pizza: String, // Identifiant de la pizza
    val id_size: String, // Identifiant de la taille
    val price: String, // Valeur du prix
    val create_date: String, // Date de création du prix
    val update_date: String, // Date de mise à jour du prix
    val size: String // Taille correspondante au prix
)
