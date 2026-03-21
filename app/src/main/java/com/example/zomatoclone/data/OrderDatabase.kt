package com.example.zomatoclone.data

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import com.example.zomatoclone.data.dao.OrderDAO
import com.example.zomatoclone.data.models.Orders
import android.content.Context
import androidx.room.RoomDatabase


@Database(entities = [Orders::class], version = 2)
abstract class OrderDatabase : RoomDatabase() {

    abstract fun orderDAO() : OrderDAO

    companion object{
        @Volatile
        private var INSTANCE : OrderDatabase ? = null

        fun getOrderDatabase(context: Context) : OrderDatabase{
            if(INSTANCE==null){
                INSTANCE = databaseBuilder(context.applicationContext,
                    OrderDatabase::class.java,
                    "orderDb")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE!!
        }


    }

}
