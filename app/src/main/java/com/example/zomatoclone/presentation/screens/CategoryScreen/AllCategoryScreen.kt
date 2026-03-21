package com.example.zomatoclone.presentation.screens.CategoryScreen


import android.content.Context
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.zomatoclone.presentation.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.navigation.NavController
import com.example.zomatoclone.data.models.AllCategoryItem
import com.example.zomatoclone.presentation.ViewModels.AddToCartViewModel
import com.example.zomatoclone.presentation.ViewModels.AllCategoryViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.components.shimmerEffect
import kotlin.math.max
import com.example.zomatoclone.R
import com.example.zomatoclone.data.UserDataStore

@Composable
fun AllCategoryScreen(navController: NavController,viewModel : AllCategoryViewModel,
                      cartViewModel: AddToCartViewModel,
                      sharedViewModel: SharedViewModel) {
    var allCategoryList by remember{ mutableStateOf<List<AllCategoryItem>?>(null) }
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val isVeg = dataStore.getVegToggle().collectAsState(initial = null).value

    LaunchedEffect(isVeg) {
        val data = viewModel.getAllCategory()
        allCategoryList = when(isVeg){
            true -> data.filter { it.veg == true}
            false -> data
            null -> data
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)

    ) {


        Row(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp, top = 8.dp)
                .background(Color.White),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Text(
                    text = "553 RESTAURANTS DELIVERING TO YOU",
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Featured",
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
                )
            }
        }
        if(allCategoryList==null){
            repeat(10){
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .shimmerEffect())
                Spacer(modifier = Modifier.height(16.dp))
            }
        }else{

            allCategoryList?.forEach { item->
                AllHomeScreenCard(navController = navController,item,cartViewModel,LocalContext.current,sharedViewModel)
                Spacer(modifier = Modifier.height(16.dp),)
            }
        }



    }
}


@Composable
fun AllHomeScreenCard(
    navController: NavController,
    item : AllCategoryItem,
    cartViewModel: AddToCartViewModel,
    context : Context,
    sharedViewModel: SharedViewModel
) {
    val pager = rememberPagerState(0){max(0,item.images.size)}
    val scope = rememberCoroutineScope()
    LaunchedEffect(pager) {
        while(true){
            delay(2500)
            scope.launch {
                val newPager = (pager.currentPage + 1 )%item.images.size
                pager.animateScrollToPage(
                    newPager,
                    animationSpec = tween(
                        800,
                        easing = LinearOutSlowInEasing
                    )
                )
            }
        }

    }
    var clicked = false



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(312.dp)
            .padding(horizontal = 16.dp)
            .background(Color.White),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
            sharedViewModel.selectedItem = item
            navController.navigate(Routes.ParticularCardScreen)
        }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()) {

                AllCardImagesRow(pager,item,context)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                    , verticalAlignment = Alignment.CenterVertically
                ) {
                    AllPriceCard(item.name, item.price.toString())




                }

                AllPageCount(pager)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 192.dp)
                        .height(120.dp)
                ) {
                    AllSmallDetailCard(item)
                    AllDetailCard(item)
                }
            }
        }
    }
}

@Composable
fun AllCardImagesRow(pagerState: PagerState,item: AllCategoryItem,context : Context) {
    HorizontalPager(state = pagerState) { page ->

        AsyncImage(
            model = item.images[page],
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
        )

    }
}

@Composable
fun AllPriceCard(name: String, price: String) {
    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth(0.7f)
            .height(22.dp)
            .clickable {},
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))

    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            androidx.compose.material3.Text(
                name, color = Color.White, fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Icon(
                painterResource(R.drawable.dot),
                modifier = Modifier.size(14.dp),
                tint = Color.White,
                contentDescription = null
            )
            androidx.compose.material3.Text("₹$price", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun AllPageCount(pagerState: PagerState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 280.dp, top = 196.dp)
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

@Composable
fun AllSmallDetailCard(item : AllCategoryItem) {
    val timedText = remember { mutableStateOf(item.time) }
    val distance = remember { mutableStateOf(item.distance) }

    Card(
        modifier = Modifier
            .size(width = 122.dp, height = 25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(topEnd = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Icon(
                painterResource(R.drawable.timer),
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
                    .size(15.dp),
                tint = Color.Green,
                contentDescription = null
            )

            androidx.compose.material3.Text(
                timedText.value,
                modifier = Modifier.padding(start = 2.dp, top = 2.dp, bottom = 2.dp),
                fontSize = 11.sp,
                color = Color.Gray
            )

            Icon(
                painterResource(R.drawable.dot),
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(18.dp),
                tint = Color.Gray,
                contentDescription = null
            )

            androidx.compose.material3.Text(
                distance.value,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun AllDetailCard(item : AllCategoryItem) {
    val restaurantName = remember { mutableStateOf(item.restaurant) }
    val rating = remember { mutableStateOf(item.rating) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
    ) {
        Column(Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                androidx.compose.material3.Text(
                    restaurantName.value,
                    color = Color.Black,
                    fontSize = 20.sp
                )

                Card(
                    modifier = Modifier.size(width = 45.dp, height = 22.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor =  Color(0xFF339B37))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.Text(
                            rating.value,
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Icon(
                            painterResource(R.drawable.star),
                            modifier = Modifier
                                .padding(start = 5.dp)
                                .size(10.dp),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 6.dp)
            ){

                Icon(
                    painterResource(R.drawable.check)
                    ,modifier = Modifier.size(16.dp),
                    tint = Color.Blue,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                androidx.compose.material3.Text(
                    text = "On time Preparation"
                )

            }
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp),
                color = colorResource(R.color.purple_200),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 6.dp)
            ) {

                Icon(
                    painterResource(R.drawable.discount),
                    modifier = Modifier.size(16.dp),
                    tint = Color.Blue,
                    contentDescription = null
                )

                Spacer(Modifier.width(4.dp))

                androidx.compose.material3.Text(
                    item.description,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showPriceCard(){
    PriceCard(name = "Veg Hakka Noodles", price = "240")
}
