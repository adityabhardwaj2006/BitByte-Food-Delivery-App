package com.example.zomatoclone.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.zomatoclone.data.models.Orders
import kotlinx.coroutines.flow.Flow


//It contains all the methods which we will do in the room database
@Dao
interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(orders: Orders)

    @Query("DELETE FROM orders WHERE id = :id")
    suspend fun deleteOrder(id : Int)

    @Query("UPDATE orders SET quantity = :qty WHERE id = :id")
    suspend fun updateOrder(qty : Int,id:Int)

    @Query("SELECT * FROM orders")
    fun getOrders() : Flow<List<Orders>>

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrder()

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrderCount() : Int

}