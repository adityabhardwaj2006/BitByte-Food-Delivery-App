package com.example.zomatoclone.presentation.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.Database
import coil.compose.AsyncImage
import com.example.zomatoclone.presentation.ViewModels.AddToCartViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.zomatoclone.R
import com.example.zomatoclone.data.OrderDatabase
import com.example.zomatoclone.data.models.Orders
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


@Preview(showBackground = true)
@Composable
fun ShowParticularPreview() {
    val navController = rememberNavController()
    ParticularCardScreen(navController, hiltViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParticularCardScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    val zomViewModel: ZomViewModel = hiltViewModel()
    val addToCartViewModel: AddToCartViewModel = hiltViewModel()
    var quantity by remember { mutableIntStateOf(1) }
    val item = sharedViewModel.selectedItem
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { item?.images?.size ?: 0 }
    )
    val database = OrderDatabase.getOrderDatabase(context)

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.padding(bottom = 20.dp),
                containerColor = Color.White,
                tonalElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Card(
                            modifier = Modifier
                                .size(48.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Black)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(
                                        onClick = {
                                            if (quantity > 1) {
                                                quantity--
                                            }
                                        }
                                    ),
                                contentAlignment = Alignment.Center

                            ) {
                                Text("-", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Box(
                            Modifier
                                .height(30.dp)
                                .clipToBounds(),
                            contentAlignment = Alignment.Center,

                            ) {
                            Text(
                                text = quantity.toString(),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Medium
                            )

                        }



                        Card(
                            modifier = Modifier
                                .size(48.dp),
                            colors = CardDefaults.cardColors(Color.White),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.Black)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(
                                        onClick = {
                                            quantity++
                                        }


                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                database.orderDAO().insertOrder(

                                    Orders(
                                        itemName = item?.name ?: "",
                                        quantity = quantity,
                                        veg = item?.veg ?: true,
                                        price = item?.price ?: 0
                                    )
                                )
                                Toast.makeText(
                                    context,
                                    "Item added to the cart",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        },
                        modifier = Modifier
                            .height(48.dp)
                            .weight(1.2f),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            Color(0xFF4CAF50),
                            contentColor = Color.White
                        )

                    ) {
                        Text(
                            text = "Add to Cart",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }


                }

            }
        },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color.White),
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(R.drawable.arrowback),
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val scope = rememberCoroutineScope()
        LaunchedEffect(pagerState.pageCount) {
            while (pagerState.pageCount > 0) {
                delay(2000)

                val newPage = (pagerState.currentPage + 1) % pagerState.pageCount

                pagerState.animateScrollToPage(newPage)
            }

        }

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Box {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                ) { page ->

                    val imageUrl = item?.images?.getOrNull(page)

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                }
                pageCount(
                    pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)
                )


            }



            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .weight(1f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.locationdeliveryscreen),
                        contentDescription = "Location",
                        modifier = Modifier.size(22.dp),
                        tint = Color.Red
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = item?.restaurant ?: "Haldiram's",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Spacer(Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.dining),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    Text(
                        text = item?.name ?: "Spring Roll",
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = ("₹" + item?.price?.toString()) ?: "₹250",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    InfoItem(
                        icon = R.drawable.star,
                        text = item?.rating.toString(),
                        tint = Color(0xFFFFC107)
                    )

                    InfoItem(
                        icon = R.drawable.timer,
                        text = item?.time.toString()
                    )

                    if (item?.veg == true) {
                        Icon(
                            painter = painterResource(R.drawable.veg),
                            contentDescription = "Vegetarian",
                            modifier = Modifier.size(45.dp),
                            tint = Color.Unspecified
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.nonveg),
                            contentDescription = "Non-Vegetarian",
                            modifier = Modifier.size(45.dp),
                            tint = Color.Unspecified
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "About this dish",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.height(6.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 80.dp)
                ) {
                    Text(
                        text = item?.description
                            ?: "Crispy on the outside and irresistibly flavorful on the inside, our Spring Rolls are a perfect blend of texture and taste. Each roll is delicately wrapped in a thin golden pastry sheet and deep-fried to perfection, giving it a satisfying crunch with every bite. Inside, you’ll discover a delicious filling of finely chopped fresh vegetables sautéed with aromatic herbs, light soy sauce, and subtle spices that enhance the natural flavors without overpowering them.\n",
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(
    icon: Int,
    text: String,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )

        Spacer(Modifier.width(4.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun pageCount(
    pagerState: PagerState,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pagerState.pageCount) { index ->
            Spacer(
                modifier = Modifier
                    .background(
                        color = if (pagerState.currentPage == index)
                            Color.White else Color.White.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
                    .size(9.dp)
            )
        }
    }
}
