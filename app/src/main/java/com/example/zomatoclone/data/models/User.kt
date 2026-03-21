package com.example.zomatoclone.data.models

data class User(
    var email:String = "",
    var name:String = "",
    var address:String = "",
    var cart : Map<String, CartItems> = mapOf(),
    var orderHistory: List<CartHistory> = listOf()
)
