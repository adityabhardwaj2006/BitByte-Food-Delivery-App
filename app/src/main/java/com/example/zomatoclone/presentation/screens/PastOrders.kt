package com.example.zomatoclone.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zomatoclone.data.models.CartHistory
import com.example.zomatoclone.data.models.Orders
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastOrders(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val user = FirebaseAuth.getInstance().currentUser?.uid
    var items by remember { mutableStateOf<List<CartHistory>>(emptyList()) }
    val zomViewModel: ZomViewModel = hiltViewModel()


    LaunchedEffect(Unit) {
        if (user != null) {
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(user)
                .get()
                .addOnSuccessListener { document ->

                    val orderList = document.get("orderHistory") as? List<Map<String, Any>>

                    items = orderList?.map { orderMap ->

                        val itemsList = orderMap["items"] as? List<Map<String, Any>>

                        CartHistory(
                            time = orderMap["time"] as? String ?: "",
                            items = itemsList?.map { item ->
                                Orders(
                                    itemName = item["itemName"] as? String ?: "",
                                    price = (item["price"] as? Long ?: 0L).toInt(),
                                    quantity = (item["quantity"] as? Long ?: 0L).toInt(),
                                    veg = item["veg"] as? Boolean ?: true
                                )
                            }
                        )
                    } ?: emptyList()
                }
        }

    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Orders",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Text(
                text = "Completed ${items.size}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier.padding(12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { order ->
                    ShowItem(order)
                }
            }
        }
    }
}

@Composable
fun ShowItem(order: CartHistory) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        val orderList = order.items
        val totalBill = orderList?.sumOf {
            it.price * it.quantity
        } ?: 0
        Text(text = "Time: ${order.time}")
        Text(text = "Total Bill: ₹${totalBill}", fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()

        Text("Items:", fontWeight = FontWeight.Bold)

        orderList?.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(item.itemName)
                    Text(
                        if (item.veg) "Veg" else "Non-Veg",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Text("x${item.quantity}  ₹${item.price}")
            }
        }
    }

}
