import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.charabot.androiderestaurant.CategoryActivity
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Votre code actuel pour afficher les boutons de catégorie
        CategoryCard("Entrées")
        CategoryDivider()
        CategoryCard("Plats")
        CategoryDivider()
        CategoryCard("Desserts")
    }
}

@Composable
fun CategoryCard(categoryName: String) {
    val context = LocalContext.current
    Text(
        text = categoryName,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .clickable {
                // Utilisez Intent pour passer à CategoryActivity
                context.startActivity(Intent(context, CategoryActivity::class.java).apply {
                    putExtra("category_name", categoryName)
                })
            },
        color = Color(0xFFF57C00),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CategoryDivider() {
    Divider(
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        thickness = 1.dp
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AndroidERestaurantTheme {
        HomeScreen()
    }
}
