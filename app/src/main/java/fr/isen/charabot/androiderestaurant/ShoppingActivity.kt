package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class ShoppingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingScreen()
                }
            }
        }
    }
}

@Composable
fun ShoppingScreen() {
    var totalPrice by remember { mutableStateOf(0f) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Total: €$totalPrice",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ProductItem(name = "Product 1", price = 10f) { price ->
            totalPrice += price
        }
        ProductItem(name = "Product 2", price = 20f) { price ->
            totalPrice += price
        }
        ProductItem(name = "Product 3", price = 15f) { price ->
            totalPrice += price
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle order button click */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Commander")
        }
    }
}

@Composable
fun ProductItem(name: String, price: Float, onPriceChange: (Float) -> Unit) {
    var quantity by remember { mutableStateOf(1) }

    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$name - €$price x $quantity",
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { quantity++ },
            modifier = Modifier.size(36.dp)
        ) {
            Text(text = "+")
        }
    }

    onPriceChange(quantity * price)
}

@Preview(showBackground = true)
@Composable
fun ShoppingPreview() {
    AndroidERestaurantTheme {
        ShoppingScreen()
    }
}
