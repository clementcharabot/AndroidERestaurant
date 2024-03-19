package fr.isen.charabot.androiderestaurant.model

data class MenuResponse(val data: List<Category>)

data class Category(val name: String, val items: List<MenuItem>)

data class MenuItem(
    val title: String,
    val description: String,
    val image: String,
    val price: String
)
