package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme

data class MenuItem(
    val id: String, // Identifiant de l'item
    val name_fr: String, // Nom français de l'item
    val id_category: String, // Identifiant de la catégorie de l'item
    val categ_name_fr: String, // Nom français de la catégorie de l'item
    val images: List<String>, // URLs des images de l'item
    val ingredients: List<Ingredient>, // Liste des ingrédients de l'item
    val prices: List<Price> // Liste des prix de l'item
) : java.io.Serializable

// Écran de détails qui montre les informations d'un plat spécifique.
class DetailCategoryActivity : ComponentActivity() {
    // Appelée à la création de l'activité.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedItem = intent.getSerializableExtra("selectedItem") as? MenuItem
        // Définit le contenu de l'activité en utilisant Jetpack Compose.
        setContent {
            // Si l'objet MenuItem est récupéré avec succès, il est utilisé pour construire l'écran de détails.
            selectedItem?.let {
                AndroidERestaurantTheme {
                    // Si l'objet MenuItem est récupéré avec succès, il est utilisé pour construire l'écran de détails.
                    selectedItem?.let { menuItem ->
                        DetailScreen(menuItem)
                    } ?: finish() // Si aucun plat n'est passé, l'activité se termine.
                }
            }
        }
    }

    @Composable
    fun DetailScreen(selectedItem: MenuItem) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImageCarousel(images = selectedItem.images)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = selectedItem.name_fr,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = selectedItem.ingredients.joinToString(separator = ", ") { it.name_fr },
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Section for quantity picker and total price
            QuantityPicker()
            TotalPrice(prices = selectedItem.prices)
        }
    }
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<String>) {
    // LazyRow crée un carrousel horizontal scrollable pour les images.
    LazyRow(
        modifier = Modifier
            .height(200.dp) // Définit la hauteur de la LazyRow.
            .fillMaxWidth() // Remplit la largeur maximale disponible.
    ) {
        items(images) { imageUrl ->
            // Utilise Coil pour charger les images de manière asynchrone avec effet de fondu.
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current).data(data = imageUrl).apply(block = fun ImageRequest.Builder.() {
                    crossfade(true)
                }).build()
            )

            // Affiche chaque image dans LazyRow avec une taille et un recadrage spécifiques.
            Image(
                painter = painter,
                contentDescription = "Image du plat",
                modifier = Modifier
                    .fillParentMaxHeight() // Remplit la hauteur disponible dans LazyRow.
                    .width(200.dp), // Définit une largeur fixe pour chaque image.
                contentScale = ContentScale.Crop // Ajuste l'image pour remplir l'espace tout en maintenant le ratio.
            )
            Spacer(modifier = Modifier.width(8.dp)) // Ajoute un espace entre chaque image.
        }
    }
  }
}

@Composable
fun QuantityPicker() {
    var quantity by remember { mutableStateOf(1) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = { if (quantity > 1) quantity-- }) {
            Text("-")
        }
        Text(
            text = "$quantity",
            modifier = Modifier
                .padding(horizontal = 16.dp)
        )
        Button(onClick = { quantity++ }) {
            Text("+")
        }
    }
}


@Composable
fun TotalPrice(prices: List<Price>) {
    val total = prices.firstOrNull()?.price ?: "0"
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        // Remplacement de 'h6' par 'titleLarge' si 'h6' n'est pas disponible
        Text("TOTAL ${total}€", style = MaterialTheme.typography.titleLarge)
    }
}