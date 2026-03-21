package com.example.zomatoclone.data.models

data class Burger(
    val description: String,
    val distance: String,
    val id: Int,
    val images: List<String>,
    val name: String,
    val price: Int,
    val rating: String,
    val restaurant: String,
    val time: String,
    val veg: Boolean
)