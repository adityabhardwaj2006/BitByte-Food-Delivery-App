package com.example.zomatoclone.data.models

data class CartHistory(
    val time: String = "",
    val items: List<Orders>? = emptyList(),
    )
