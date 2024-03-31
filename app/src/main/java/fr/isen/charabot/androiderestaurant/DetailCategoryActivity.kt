package fr.isen.charabot.androiderestaurant

import ShoppingActivity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject

class DetailCategoryActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val menuItemJson = intent.getStringExtra("menuItem")
        val menuItem = Gson().fromJson(menuItemJson, MenuItem::class.java)

        setContent {
            AndroidERestaurantTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("") },
                            actions = {
                                IconButton(
                                    onClick = {
                                        val totalPrice = menuItem.getPriceFormatted(1) // Assuming quantity is 1
                                        val intent = Intent(this@DetailCategoryActivity, ShoppingActivity::class.java)
                                        intent.putExtra("totalPrice", totalPrice)
                                        startActivity(intent)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ShoppingCart,
                                        contentDescription = "Cart"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    MenuItemDetails(menuItem = menuItem)
                }
            }
        }
    }
}

@Composable
private fun fetchMenuItems(categoryName: String, onResult: (List<MenuItem>) -> Unit) {
    val queue = Volley.newRequestQueue(LocalContext.current)
    val url = "http://test.api.catering.bluecodegames.com/menu?category=$categoryName"

    val jsonObjectRequest = JsonObjectRequest(
        Request.Method.GET, url, null,
        { response ->
            // Traitement de la réponse JSON
            val menuItems = parseMenuItems(response = response)
            onResult(menuItems)
        },
        { error ->
            // Gestion des erreurs
            error.printStackTrace()
            onResult(emptyList()) // En cas d'erreur, retournez une liste vide
        })

    queue.add(jsonObjectRequest)
}

private fun parseMenuItems(response: JSONObject): List<MenuItem> {
    return emptyList()
}

@Composable
fun MenuItemDetails(menuItem: MenuItem) {
    var quantity by remember { mutableStateOf(1) }
    var totalPrice by remember { mutableStateOf(menuItem.getFirstPriceFormatted()) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(data = menuItem.images.firstOrNull()),
                contentDescription = menuItem.name_fr,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = menuItem.name_fr,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = menuItem.getFormattedIngredients(),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(Modifier.height(16.dp))
            QuantitySelector(quantity = quantity, onQuantityChange = { newQuantity ->
                quantity = newQuantity
                totalPrice = menuItem.getPriceFormatted(quantity)
            })
            Spacer(Modifier.height(8.dp))
            AddToCartButton(totalPrice = totalPrice, onClick = { /* Define action here */ })
        }
    }
}

@Composable
fun AddToCartButton(totalPrice: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(Color(0xFFFFA500)),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Total : $totalPrice",
                color = Color.White
            )
        }
    }
}

fun MenuItem.getFormattedIngredients(): String {
    return ingredients.joinToString(separator = ", ") { it.name_fr.capitalize() }
}

fun MenuItem.getFirstPriceFormatted(): String {
    return prices.firstOrNull()?.price ?: "Price not available"
}

fun MenuItem.getPriceFormatted(quantity: Int): String {
    val firstPrice =
        prices.firstOrNull()?.price?.toFloatOrNull() ?: return "Price not available"
    val totalPrice = firstPrice * quantity
    return String.format("%.2f€", totalPrice)
}

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
            modifier = Modifier.background(Color(0xFFFFA500), shape = CircleShape)
        ) {
            Text(
                text = "-",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "$quantity",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier.background(Color(0xFFFFA500), shape = CircleShape)
        ) {
            Text(
                text = "+",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}
