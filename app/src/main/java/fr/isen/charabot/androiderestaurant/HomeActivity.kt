package fr.isen.charabot.androiderestaurant

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    @Composable
    fun HomeScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = "Bienvenue chez",
                        fontWeight = FontWeight.Light,
                        fontSize = 30.sp,
                        color = Color(0xFFCCA37C)
                    )
                    Text(
                        text = "DroidRestaurant",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = Color(0xFFE08E45)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.androidcook), // Remplacez par votre ressource image
                    contentDescription = "Restaurant Mascot",
                    modifier = Modifier.size(150.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Spacer pour pousser les éléments vers le haut ou le bas

            // Les boutons "Entrées", "Plats" et "Desserts" sont centrés avec des dividers entre eux
            CategoryCard("Entrées")
            CategoryDivider()
            CategoryCard("Plats")
            CategoryDivider()
            CategoryCard("Desserts")

            Spacer(modifier = Modifier.weight(1f)) // Spacer pour pousser les éléments vers le haut ou le bas
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
                    context.startActivity(Intent(context, CategoryActivity::class.java).apply {
                        putExtra("category_name", categoryName)
                    })
                },
            color = Color(0xFFF57C00), // Couleur orange, ajustez selon votre thème
            textAlign = TextAlign.Center // Texte centré horizontalement
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
}
