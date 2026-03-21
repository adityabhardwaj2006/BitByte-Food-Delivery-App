package com.example.zomatoclone.data.models

data class UserData(
    val email :String,
    val password : String,
    val name : String,
    val address : String,
    val cart : Map<String, Long> = emptyMap()

)

