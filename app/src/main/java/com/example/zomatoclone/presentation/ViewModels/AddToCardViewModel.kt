package com.example.zomatoclone.presentation.ViewModels

import android.annotation.SuppressLint
import android.widget.Toast
import com.example.zomatoclone.common.USER_COLLECTION
import android.content.Context
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.collections.mapOf

@HiltViewModel
class AddToCartViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
)  : ViewModel(){


    @SuppressLint("SuspiciousIndentation")
    fun addToCart(context : Context, itemName : String, quantity :Int,veg : Boolean, price : Int){
        val currUser = FirebaseAuth.getInstance().currentUser?.uid

        if (currUser == null) {
            Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show()
            return
        }
        val itemData = mapOf("ItemName" to itemName,
            "Quantity" to quantity,
            "Veg" to veg,
            "Price" to price)
        val userDoc = Firebase.firestore
            .collection(USER_COLLECTION)
            .document(currUser)
            .update("cart.$itemName",itemData)
            .addOnSuccessListener {
                Toast.makeText(context,"Item added to the cart" , Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                Toast.makeText(context,"Error : ${e.message}", Toast.LENGTH_SHORT).show()
            }



    }


}
