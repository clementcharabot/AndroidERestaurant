import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.gson.Gson

class ShoppingActivity : ComponentActivity() {

    private val preferencesKey = "cart_items_count"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get total price from intent
        val totalPrice = intent.getStringExtra("totalPrice") ?: ""

        // Get cart items count from preferences
        val preferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        var cartItemsCount by remember { mutableStateOf(preferences.getInt(preferencesKey, 0)) }

        // Set content with ShoppingScreen composable
        setContent {
            ShoppingScreen(
                totalPrice = totalPrice,
                cartItemsCount = cartItemsCount,
                onCartItemsCountChange = { newCount ->
                    cartItemsCount = newCount
                    preferences.edit().putInt(preferencesKey, newCount).apply()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    totalPrice: String,
    cartItemsCount: Int,
    onCartItemsCountChange: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Shopping Cart") },
                navigationIcon = {
                    // Add icon to redirect to cart
                    CartIcon(cartItemsCount) {
                        // Handle click to navigate to cart
                        // You can navigate to CartActivity or any other activity related to the cart
                    }
                }
            )
        }
    ) content {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Total : $totalPrice",
                style = MaterialTheme.typography.headline6
            )
            // Display cart items here
        }
    }
}

@Composable
fun CartIcon(cartItemsCount: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        // Display cart icon with badge
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Outlined.ShoppingCart,
                contentDescription = "Shopping Cart"
            )
            if (cartItemsCount > 0) {
                Badge(count = cartItemsCount)
            }
        }
    }
}

@Composable
fun Badge(count: Int) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .padding(start = 16.dp, top = 4.dp),
        contentAlignment = Alignment.Center,
        content = {
            Text(
                text = count.toString(),
                color = Color.White,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(4.dp)
            )
        },
        backgroundColor = MaterialTheme.colors.secondary,
        shape = MaterialTheme.shapes.small
    )
}
