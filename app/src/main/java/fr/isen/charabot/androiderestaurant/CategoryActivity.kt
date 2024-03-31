package fr.isen.charabot.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
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
            val isLoading = remember { mutableStateOf(true) }
            val menuItems = remember { mutableStateOf<List<MenuItem>>(listOf()) }
            val context = LocalContext.current

            AndroidERestaurantTheme {
                Column {
                    if (isLoading.value) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    } else {
                        MenuScreen(categoryName = categoryName, items = menuItems.value, onClick = { menuItem ->
                            val intent = Intent(context, DetailCategoryActivity::class.java).apply {
                                val gson = Gson()
                                val menuItemJson = gson.toJson(menuItem)
                                putExtra("menuItem", menuItemJson)
                            }
                            context.startActivity(intent)
                        })
                    }
                }
            }

            fetchMenuItems(categoryName) { items ->
                menuItems.value = items
                isLoading.value = false
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
                Log.d("CategoryActivity", "Réponse de l'API: $response")
                try {
                    val gson = Gson()
                    val menuResponse = gson.fromJson(response.toString(), MenuResponse::class.java)
                    val filteredItems =
                        menuResponse.data.firstOrNull { it.name_fr == categoryName }?.items
                            ?: emptyList()
                    onResult(filteredItems)
                } catch (e: Exception) {
                    Log.e("CategoryActivity", "Parsing error", e)
                    onResult(emptyList())
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("CategoryActivity", "Volley error: ${error.message}")
                runOnUiThread {
                    Toast.makeText(this, "Failed to load data: ${error.message}", Toast.LENGTH_LONG).show()
                }
                onResult(emptyList())
            })

        queue.add(jsonObjectRequest)
    }
}

@Composable
fun MenuScreen(categoryName: String, items: List<MenuItem>, onClick: (MenuItem) -> Unit) {
    Column {
        Text(
            text = categoryName,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(items) { item ->
                MenuItemComposable(item = item, onClick = onClick)
            }
        }
    }
}

@Composable
fun MenuItemComposable(item: MenuItem, onClick: (MenuItem) -> Unit) {
    Column(modifier = Modifier
        .padding(8.dp)
        .clickable { onClick(item) }) {
        if (item.images.isNotEmpty()) {
            ImageFromUrls(urls = item.images)
        } else {
            // Placeholder si aucune image n'est disponible
            Text(text = "Image non disponible")
        }
        Text(
            text = item.name_fr,
            modifier = Modifier.padding(top = 8.dp)
        )
        // Affichage du prix avec le signe €
        Text(
            text = "${item.getFirstPriceFormatted()} €", // Ajout du signe € avant le prix
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}



@Composable
fun ImageFromUrls(urls: List<String>, imageDefault: Int = R.drawable.error) {
    var currentUrlIndex by remember { mutableStateOf(0) }
    val painter = rememberImagePainter(
        data = urls.getOrNull(currentUrlIndex) ?: "",
        builder = {
            crossfade(true)
            error(imageDefault)
            placeholder(imageDefault)
            listener(onError = { _, _ ->
                if (currentUrlIndex < urls.size - 1) {
                    currentUrlIndex++
                }
            })
        }
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

data class MenuResponse(
    val data: List<Category>
)

data class Category(
    val name_fr: String,
    val items: List<MenuItem>
)

data class MenuItem(
    val id: String,
    val name_fr: String,
    val id_category: String,
    val categ_name_fr: String,
    val images: List<String>,
    val ingredients: List<Ingredient>,
    val prices: List<Price>
)

data class Ingredient(
    val id: String,
    val id_shop: String,
    val name_fr: String,
    val create_date: String,
    val update_date: String,
    val id_pizza: String?
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
