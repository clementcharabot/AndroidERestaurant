package fr.isen.charabot.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import fr.isen.charabot.androiderestaurant.ui.theme.AndroidERestaurantTheme
import coil.compose.rememberImagePainter // Import correct pour Coil
import com.google.accompanist.pager.ExperimentalPagerApi

data class MenuItem(
    val id: String, // Identifiant de l'item
    val name_fr: String, // Nom français de l'item
    val id_category: String, // Identifiant de la catégorie de l'item
    val categ_name_fr: String, // Nom français de la catégorie de l'item
    val images: List<String>, // URLs des images de l'item
    val ingredients: List<Ingredient>, // Liste des ingrédients de l'item
    val prices: List<Price> // Liste des prix de l'item
) : java.io.Serializable

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val selectedItem = intent.getSerializableExtra("selectedItem") as? MenuItem

        setContent {
            selectedItem?.let {
                AndroidERestaurantTheme {
                    DetailScreen(it)
                }
            } ?: run {
                finish()
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
        // ... Affichez les autres détails de votre `selectedItem` ici
        Text(text = selectedItem.name_fr, modifier = Modifier.padding(top = 16.dp))
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = images.size)

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) { page ->
        val imageUrl = images[page]
        val painter = rememberImagePainter(
            data = imageUrl,
            builder = {
                crossfade(true)
            }
        )

        Image(
            painter = painter,
            contentDescription = "Image du plat",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}