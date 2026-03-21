package com.example.zomatoclone.common

//sealed class ke andar sirf voh chize atti hai jo limit mai ho
// jese yaha succes , error , loading ke alawa koi state nhi hogi tho voh ayege
sealed class ResultState<out T>{
    data class Success<T>(val data : T): ResultState<String>()
    data class Error<T>(val message : T) :ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}
