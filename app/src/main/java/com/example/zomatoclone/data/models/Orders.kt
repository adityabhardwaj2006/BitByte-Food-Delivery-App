package com.example.zomatoclone.data.models

import android.content.Context
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Orders",
    indices = [Index(value = ["itemName"], unique = true)]
)
data class Orders(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val itemName : String = "",
    val quantity : Int = 0,
    val veg : Boolean = true,
    val price : Int = 0
)

