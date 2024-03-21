package fr.isen.charabot.androiderestaurant

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    HomeScreen(context = this)
                }
            }
        }
    }

    @Composable
    fun HomeScreen(context: Context) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)), // Assuming this is the background color of the image
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            GreetingSection() // This will show the title and the mascot at the top
            Spacer(modifier = Modifier.height(64.dp)) // Fixed space after the greeting section
            CategoryButtons(context = context)
            Spacer(modifier = Modifier.height(64.dp)) // Fixed space after the category buttons
        }
    }


    @Composable
    fun GreetingSection() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Bienvenue chez",
                    color = Color(0xFF5D4037), // Use the appropriate text color
                    fontSize = 24.sp
                )
                Text(
                    text = "DroidRestaurant",
                    color = Color(0xFF5D4037), // Use the appropriate text color
                    fontSize = 24.sp,
                    fontWeight = Bold
                )
            }
            Image(
                painter = painterResource(id = R.drawable.androidcook), // Replace with the correct resource ID
                contentDescription = "Restaurant Mascot",
                modifier = Modifier
                    .size(100.dp) // Adjust the size as needed
            )
        }
    }
    @Composable
    fun CategoryButtons(context: Context) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val categories = listOf("Entrées", "Plats", "Desserts")
            categories.forEach { category ->
                CategoryItem(name = category, context = context)
                if (category != categories.last()) {
                    CategoryDivider()
                }
            }
        }
    }

    @Composable
    fun CategoryItem(name: String, context: Context) {
        Text(
            text = name,
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(context, CategoryActivity::class.java).apply {
                        putExtra("categoryName", name)
                    }
                    context.startActivity(intent)
                }
                .padding(vertical = 16.dp)
        )
    }

    @Composable
    fun CategoryDivider() {
        Divider(
            color = Color(0xFFBCAAA4),
            modifier = Modifier
                .width(300.dp)
                .padding(8.dp),
            thickness = 2.dp
        )
    }
}
