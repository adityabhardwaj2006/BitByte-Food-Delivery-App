package com.example.zomatoclone.presentation.components


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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.zomatoclone.presentation.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.example.zomatoclone.R

@Composable
fun HomeScreenCard(
    navController: NavController,
) {
    val pager = rememberPagerState(0){4}
    val scope = rememberCoroutineScope()
    LaunchedEffect(pager) {
        while(isActive){
            delay(2500)
            scope.launch {
                val newPager = (pager.currentPage + 1 )%4
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




    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {
        }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()) {

                CardImagesRow(pager)



                PageCount(pager)


            }
        }
    }
}

@Composable
fun CardImagesRow(pagerState: PagerState) {
    HorizontalPager(state = pagerState) { page ->
        val image = when (page) {
            0 -> R.drawable.veg_biryani
            1 -> R.drawable.brick_oven_pizza
            2 -> R.drawable.spring_roll
            else -> R.drawable.chowmein1
        }

        AsyncImage(
            model = image,
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null
        )
    }
}

@Composable
fun PriceCard(name: String, price: String) {
    Card(
        modifier = Modifier
            .padding(top = 12.dp)
            .size(width = 160.dp, height = 21.dp)
            .clickable {},
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(name, color = Color.White, fontSize = 12.sp)
            Icon(
                painterResource(R.drawable.dot),
                modifier = Modifier.size(16.dp),
                tint = Color.White,
                contentDescription = null
            )
            Text(price, color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun PageCount(pagerState: PagerState) {
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
fun SmallDetailCard() {
    val timedText = remember { mutableStateOf("20 mins" )}
    val distance = remember { mutableStateOf("2.3 km") }

    Card(
        modifier = Modifier
            .size(width = 122.dp, height = 25.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(topEnd = 12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {

            Icon(
                painterResource(R.drawable.timer),
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
                    .size(15.dp),
                tint = Color.Green,
                contentDescription = null
            )

            Text(
                timedText.value,
                modifier = Modifier.padding(start = 2.dp, top = 2.dp, bottom = 2.dp),
                fontSize = 11.sp,
                color = Color.Gray
            )

            Icon(
                painterResource(R.drawable.dot),
                modifier = Modifier.padding(top = 4.dp).size(18.dp),
                tint = Color.Gray,
                contentDescription = null
            )

            Text(
                distance.value,
                modifier = Modifier.padding(top = 2.dp, bottom = 2.dp),
                fontSize = 11.sp,
                color = Color.Gray
            )
        }
    }
}
@Composable
fun DetailCard() {
    val restaurantName = remember { mutableStateOf("Haldiram's") }
    val rating = remember { mutableStateOf(4.6) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
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
                Text(
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
                        Text(
                            rating.toString(),
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 12.sp,
                            color = Color.White
                        )
                        Icon(
                            painterResource(R.drawable.star),
                            modifier = Modifier.padding(start = 5.dp).size(10.dp),
                            tint = Color.White,
                            contentDescription = null
                        )
                    }
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 18.dp, top = 6.dp)
            ){

                Icon(
                    painterResource(R.drawable.check)
                            ,modifier = Modifier.size(16.dp),
                    tint = Color.Blue,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "On time Preparation"
                )

            }
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 6.dp),
                color = colorResource(R.color.purple_200),
                thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 6.dp)
            ) {

                Icon(
                    painterResource(R.drawable.discount),
                    modifier = Modifier.size(16.dp),
                    tint = Color.Blue,
                    contentDescription = null
                )

                Spacer(Modifier.width(4.dp))

                Text(
                    "Authentic Delhi-style spicy chickpeas served with two fluffy bhaturas",
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray,
                )
            }
        }
    }
}

