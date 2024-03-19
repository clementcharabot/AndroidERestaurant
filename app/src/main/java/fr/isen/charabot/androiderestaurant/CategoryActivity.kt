package fr.isen.charabot.androiderestaurant

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject
import fr.isen.charabot.androiderestaurant.model.*
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("categoryName") ?: "Catégorie inconnue"

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
    var menuItems by remember { mutableStateOf(listOf<MenuItem>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(categoryName) {
        coroutineScope.launch {
            fetchMenuItems(context, categoryName) { items ->
                menuItems = items
            }
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = categoryName, modifier = Modifier.padding(bottom = 8.dp))
        menuItems.forEach { menuItem ->
            Text(text = menuItem.title, modifier = Modifier.padding(bottom = 4.dp))
            Image(
                painter = rememberImagePainter(menuItem.image),
                contentDescription = menuItem.title,
                modifier = Modifier.size(128.dp)
            )
        }
    }
}

fun fetchMenuItems(context: Context, categoryName: String, onResult: (List<MenuItem>) -> Unit) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://test.api.catering.bluecodegames.com/menu"
    val jsonBody = JSONObject()
    jsonBody.put("id_shop", "1")

    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonBody,
        Response.Listener { response ->
            val menuResponse = Gson().fromJson(response.toString(), MenuResponse::class.java)
            val items = menuResponse.data.find { category -> category.name == categoryName }?.items ?: emptyList()

        },
        Response.ErrorListener { error ->
            Toast.makeText(context, "Erreur : ${error.message}", Toast.LENGTH_LONG).show()
        }
    ) {
        override fun getHeaders(): Map<String, String> {
            return mapOf("Content-Type" to "application/json")
        }
    }

    queue.add(jsonObjectRequest)
}
