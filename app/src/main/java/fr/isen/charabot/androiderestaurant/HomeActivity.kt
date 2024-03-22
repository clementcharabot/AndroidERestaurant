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

// Écran d'accueil de l'application qui affiche la section de salutation et les boutons des catégories.
class HomeActivity : ComponentActivity() {
    // Cette méthode est appelée lors de la création de l'activité.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Définit le contenu de l'activité en utilisant Jetpack Compose.
        setContent {
            // Applique le thème de l'application à l'écran d'accueil.
            AndroidERestaurantTheme {
                // Surface est un conteneur Composable qui utilise le schéma de couleurs du thème.
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Appelle la fonction Composable HomeScreen pour construire l'écran.
                    HomeScreen(context = this)
                }
            }
        }
    }

    // Fonction Composable qui construit l'écran d'accueil.
    @Composable
    fun HomeScreen(context: Context) {
        // Utilise une colonne pour disposer les éléments verticalement.
        Column(
            modifier = Modifier
                .fillMaxSize() // La colonne occupe toute la taille disponible.
                .background(Color(0xFFFFFFFF)), // Définit la couleur d'arrière-plan de la colonne.
            horizontalAlignment = Alignment.CenterHorizontally, // Centre les enfants horizontalement.
            verticalArrangement = Arrangement.Top // Alignement vertical en haut.
        ) {
            GreetingSection() // Affiche la section de salutation avec le texte de bienvenue.
            Spacer(modifier = Modifier.height(64.dp)) // Espace fixe après la section de salutation.
            CategoryButtons(context = context) // Affiche les boutons des catégories (Entrées, Plats, Desserts).
            Spacer(modifier = Modifier.height(64.dp)) // Espace fixe après les boutons des catégories.
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
