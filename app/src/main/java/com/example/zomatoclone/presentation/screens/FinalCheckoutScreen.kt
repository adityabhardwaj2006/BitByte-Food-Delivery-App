package com.example.zomatoclone.presentation.screens

import android.app.Activity
import android.content.Context
import com.google.firebase.firestore.SetOptions
import com.example.zomatoclone.R
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zomatoclone.PaymentState
import com.example.zomatoclone.common.USER_COLLECTION
import com.example.zomatoclone.data.OrderDatabase
import com.example.zomatoclone.data.UserDataStore
import com.example.zomatoclone.data.models.CartHistory
import com.example.zomatoclone.data.models.CartItems
import com.example.zomatoclone.data.models.Orders
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.presentation.components.shimmerEffect
import com.example.zomatoclone.presentation.navigation.Routes
import com.example.zomatoclone.presentation.navigation.SubNavigation
import com.example.zomatoclone.presentation.utils.OrderPlacedDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.razorpay.Checkout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.emptyList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalCheckoutScreen(
    navController: NavController,
    listState: LazyListState,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current
    val activity = context as Activity
    val dataStore = UserDataStore(context)
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val paymentSuccess = PaymentState.isSuccess.value
    val database = remember { OrderDatabase.getOrderDatabase(context) }

    val items by database.orderDAO()
        .getOrders()
        .collectAsState(initial = emptyList())
    LaunchedEffect(paymentSuccess) {
        if (paymentSuccess) {
            deleteCartAndSave(
                navController,
                items,
                database
            ) {
                isLoading = false
            }

            PaymentState.isSuccess.value = false
        }
    }


    val currUser = FirebaseAuth.getInstance().currentUser?.uid
    if (currUser == null) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Please Login", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        return
    }


    val isCartEmpty = items.isEmpty()
    val totalPrice = items.sumOf { it.price * it.quantity }

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Routes.DeliveryScreen)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = "Back",
                            modifier = Modifier.size(22.dp),
                            tint = Color.DarkGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        bottomBar = {

            if (!isCartEmpty && !isLoading) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(bottom = 58.dp)
                ) {

                    Button(
                        onClick = {
//                            showDialog = true
                            //Razorpay payment

                            startPayment(activity, totalPrice + 30)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF27A745)
                        )
                    ) {
                        Text(
                            "Place Order",
                            color = Color.White,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            if (showDialog) {
                OrderPlacedDialog(
                    onDismiss = {
                        isLoading = true
                        deleteCartAndSave(
                            navController,
                            items,
                            database
                        ) {
                            isLoading = false
                        }
                        showDialog = false
                    },
                    onConfirm = {
                        isLoading = true
                        deleteCartAndSave(
                            navController,
                            items,
                            database
                        ) {
                            isLoading = false
                        }
                        showDialog = false
                    }
                )
            }
        }

    ) { innerPadding ->

        when {

            isLoading -> {
                CheckoutShimmer(innerPadding)
            }

            isCartEmpty -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Your cart is waiting for delicious items",
                        fontSize = 18.sp
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding() + 140.dp
                    )
                ) {

                    items(items) { order ->

                        ProductCard(
                            productName = order.itemName,
                            price = order.price,
                            quantity = order.quantity,
                            veg = order.veg,
                            onQuantityChange = { newQty ->

                                scope.launch {

                                    if (newQty <= 0) {
                                        database.orderDAO().deleteOrder(order.id)
                                    } else {
                                        database.orderDAO().updateOrder(
                                            newQty,
                                            order.id
                                        )
                                    }
                                }
                            }
                        )
                    }

                    item {

                        Spacer(Modifier.height(6.dp))

                        CouponCard()

                        AddressAndBillCard(
                            time = "30 minutes",
                            price = totalPrice + 30
                        )
                    }
                }
            }
        }
    }
}

fun startPayment(activity: Activity, amount: Int) {
    val checkout = Checkout()
    checkout.setKeyID("rzp_test_STCScPiOykxRJG")

    try {
        val options = JSONObject()

        options.put("name", "BitByte")
        options.put("description", "Food Order Payment")
        options.put("currency", "INR")
        options.put("amount", amount * 100)

        val prefill = JSONObject()
        prefill.put("email", "adi@gmail.com")
        prefill.put("contact", "9999999999")

        options.put("prefill", prefill)

        checkout.open(activity, options)

    } catch (e: Exception) {
        Log.e("Razorpay", "Error: ", e)
    }
}

fun firebaseUpdate(itemName: String, qty: Int) {
    val currUser = FirebaseAuth.getInstance().currentUser?.uid
    if (currUser == null) return
    val getDoc = FirebaseFirestore.getInstance()
        .collection("users")
        .document(currUser)
    if (qty == 0) {

        getDoc.update(FieldPath.of("cart", itemName), FieldValue.delete())


    } else {
        getDoc.update(FieldPath.of("cart", itemName, "Quantity"), qty)
    }

}


fun deleteCartAndSave(
    navController: NavController,
    items: List<Orders>,
    database: OrderDatabase,
    onFinished: () -> Unit
) {

    val currUser = FirebaseAuth.getInstance().currentUser?.uid ?: return

    val firestore = FirebaseFirestore.getInstance()

    val docRef = firestore
        .collection("users")
        .document(currUser)

    val time = SimpleDateFormat(
        "dd MMM yyyy HH:mm",
        Locale.getDefault()
    ).format(Date())

    val itemsList = items.map {
        hashMapOf(
            "itemName" to it.itemName,
            "price" to it.price,
            "quantity" to it.quantity,
            "veg" to it.veg
        )
    }

    val cartHistoryMap = hashMapOf(
        "time" to time,
        "items" to itemsList
    )

    docRef.update(
        "orderHistory",
        FieldValue.arrayUnion(cartHistoryMap)
    )
        .addOnSuccessListener {

            CoroutineScope(Dispatchers.IO).launch {

                database.orderDAO().deleteAllOrder()

                withContext(Dispatchers.Main) {

                    onFinished()

                    navController.navigate(SubNavigation.MainHomeScreen) {
                        popUpTo(SubNavigation.MainHomeScreen) {
                            inclusive = true
                        }
                    }
                }
            }
        }

        .addOnFailureListener { e ->

            Log.e("FIREBASE", "Update failed: ${e.message}")

            docRef.set(
                mapOf(
                    "orderHistory" to listOf(cartHistoryMap)
                ),
                SetOptions.merge()
            )

            onFinished()
        }
}

@Composable
fun ProductCard(
    productName: String,
    price: Int,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    veg: Boolean
) {


    val snackBarState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, color = Color.DarkGray),

        ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top
            ) {

                Icon(
                    painter = if (veg == true) {
                        painterResource(R.drawable.veg)
                    } else {
                        painterResource(R.drawable.nonveg)
                    },
                    contentDescription = if (veg == true) {
                        "Veg"
                    } else {
                        "Non Veg"
                    },

                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(16.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = productName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )


                    }

                    Text(
                        text = "₹" + (price).toString(),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 4.dp)
                ) {

                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(80.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                "-", fontSize = 22.sp, modifier = Modifier
                                    .clickable {
                                        onQuantityChange(quantity - 1)
                                    })
                            Text(quantity.toString(), fontSize = 16.sp)
                            Text(
                                "+", fontSize = 20.sp, modifier = Modifier
                                    .clickable {
                                        onQuantityChange(quantity + 1)
                                    })
                        }

                    }

                }
            }
            Spacer(modifier = Modifier.width(8.dp))


        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun CouponCard() {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(
                onClick = {


                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.coupons),
                    contentDescription = "Coupons",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(18.dp)
                )

                Text(
                    text = "View all coupons",
                    fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable(
                            onClick = {
                                Toast.makeText(context, "No coupons available", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                )
            }


        }
    }
}


@Composable
fun AddressAndBillCard(time: String, price: Int) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.timer),
                    contentDescription = "Delivery Time",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Delivery under 30 minutes",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.SemiBold
                    )


                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            /* Delivery Address*/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Home,
                        contentDescription = "Home",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Delivery at Home",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.SemiBold
                        )


                    }
                }


            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            /*Contact Information*/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Phone",
                        tint = Color.DarkGray,
                        modifier = Modifier.size(18.dp)
                    )

                    Text(
                        text = "Aditya Bhardwaj\n(Restaurant Owner)",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 8.dp, end = 12.dp)
                    )

                    Text(
                        text = "+91 7827349380",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 8.dp),
                        textAlign = TextAlign.End


                    )
                }

            }

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.notes),
                            contentDescription = "Bill",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(18.dp)
                        )

                        Text(
                            text = "Total Bill : $price",
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Text(
                        text = "Incl. taxes and charges",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 26.dp)
                    )
                }

            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun showQuantButton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, color = Color.DarkGray),

        ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.Top
            ) {

                Icon(
                    painter = if (true) {
                        painterResource(R.drawable.veg)
                    } else {
                        painterResource(R.drawable.nonveg)
                    },
                    contentDescription = if (true) {
                        "Veg"
                    } else {
                        "Non Veg"
                    },

                    tint = Color.Unspecified,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(16.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Spring Roll",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "×1",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )

                    }

                    Text(
                        text = "₹" + (40).toString(),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(end = 4.dp)
                ) {

                    Card(
                        modifier = Modifier
                            .height(40.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(80.dp),
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("-", fontSize = 22.sp)
                            Text("4", fontSize = 16.sp)
                            Text("+", fontSize = 20.sp)
                        }

                    }


                }
            }

            Spacer(modifier = Modifier.width(8.dp))


        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun CheckoutShimmer(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = innerPadding.calculateTopPadding() + 8.dp,
                start = 16.dp,
                end = 16.dp
            )
    ) {

        // Row 1
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shimmerEffect()
            )

            Spacer(Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(160.dp)
                        .height(14.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(12.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Row 2
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shimmerEffect()
//                    .background(Color.LightGray, CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(14.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
                Box(
                    modifier = Modifier
                        .width(130.dp)
                        .height(12.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shimmerEffect()
//                    .background(Color.LightGray, CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(14.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
                Box(
                    modifier = Modifier
                        .width(110.dp)
                        .height(12.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // Row 4
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shimmerEffect()
//                    .background(Color.LightGray, CircleShape)
            )

            Spacer(Modifier.width(12.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(
                    modifier = Modifier
                        .width(140.dp)
                        .height(14.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(12.dp)
                        .shimmerEffect()
//                        .background(Color.LightGray, RoundedCornerShape(6.dp))
                )
            }
        }
    }
}
