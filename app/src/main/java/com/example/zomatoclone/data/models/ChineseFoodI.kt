package com.example.zomatoclone.data.models

data class ChineseFood(
    val id: Int,
    val name: String,
    val restaurant: String,
    val rating: String,
    val distance: String,
    val time: String,
    val description: String,
    val price: Int,
    val veg: Boolean,
    val images: List<String>
)


// data module , repo , repoimpl , domainModule