package com.example.zomatoclone.presentation.screens.CategoryScreen

import android.content.Context
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.zomatoclone.R
import com.example.zomatoclone.data.UserDataStore
import com.example.zomatoclone.data.models.AllCategoryItem
import com.example.zomatoclone.data.models.Biryani
import com.example.zomatoclone.presentation.ViewModels.AddToCartViewModel
import com.example.zomatoclone.presentation.ViewModels.BiryaniViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.components.shimmerEffect
import com.example.zomatoclone.presentation.navigation.Routes
import com.google.accompanist.pager.ExperimentalPagerApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun BiryaniCategoryScreen (navController: NavController,
viewModel: BiryaniViewModel,
cartViewModel: AddToCartViewModel,
sharedViewModel: SharedViewModel){
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val isVeg = dataStore.getVegToggle().collectAsState(initial = null).value
    var burgerList by remember { mutableStateOf<List<Biryani>?>(null) }
    LaunchedEffect(isVeg) {
        val data = viewModel.getBiryaniList()
        burgerList = when(isVeg){
            true -> data.filter { it.veg == true}
            false -> data
            null -> data
        }
    }
    val lazyListState = rememberLazyListState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {


        Text(
            text = "RECOMMENDED FOR YOU",
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal,
                letterSpacing = 2.sp,
                fontFamily = FontFamily.SansSerif
            ),
            modifier = Modifier
                .padding(vertical = 16.dp)
                .padding(start = 10.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(18.dp))

        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {

                Text(
                    text = "553 RESTAURANTS NEAR YOU",
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray
                )

                Text(
                    text = "Top Picks",
                    modifier = Modifier.padding(start = 5.dp),
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold

                )

                Spacer(modifier = Modifier.height(16.dp))

                if(burgerList==null){
                    repeat(5){
                        Box(modifier = Modifier.fillMaxWidth()
                            .height(250.dp)
                            .shimmerEffect())
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }else{
                    burgerList?.forEach {item->
                        BiryaniFoodCard(navController,item,cartViewModel, LocalContext.current,sharedViewModel)
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun BiryaniFoodCard(
    navController: NavController,
    item: Biryani,
    cartViewModel: AddToCartViewModel,
    context: Context,
    sharedViewModel: SharedViewModel
) {


    val pager = rememberPagerState(0){item.images.size}
    val scope = rememberCoroutineScope()
    LaunchedEffect(pager) {
        while(isActive){
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



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(312.dp)
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {

            sharedViewModel.selectedItem = AllCategoryItem(
                category = "",
                description = item.description,
                distance = item.distance,
                id = item.id,
                images = item.images,
                name = item.name,
                price = item.price,
                rating = item.rating,
                restaurant = item.restaurant,
                time = item.time,
                veg = item.veg
            )
            navController.navigate(Routes.ParticularCardScreen)
        }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()) {

                BiryaniCardImagesRow(pager,item,context)


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                    , verticalAlignment = Alignment.CenterVertically
                ) {

                    BiryaniPriceCard(name = item.name, price = item.price.toString())



                }

                BiryaniPageCount(pager)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 192.dp)
                        .height(120.dp)
                ) {
                    BiryaniSmallDetailCard(item)
                    BiryaniDetailCard(item)
                }
            }
        }
    }
}


@Composable
fun BiryaniPriceCard(name:String,price :String) {

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
            Text(name, color = Color.White, fontSize = 14.sp,
                textAlign = TextAlign.Center)
            Icon(
                painterResource(R.drawable.dot),
                modifier = Modifier.size(14.dp),
                tint = Color.White,
                contentDescription = null
            )
            Text("₹$price", color = Color.White, fontSize = 12.sp)
        }
    }
}

@Composable
fun BiryaniPageCount(pagerState: PagerState) {
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
fun BiryaniSmallDetailCard(item: Biryani) {
    val timedText by remember { mutableStateOf(item.time) }
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
                modifier = Modifier.padding(start = 5.dp, top = 5.dp, bottom = 5.dp)
                    .size(15.dp),
                tint = Color.Green,
                contentDescription = null
            )

            Text(
                timedText,
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
fun BiryaniDetailCard(item : Biryani) {
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
                            rating.value,
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
                    text = item.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}

@Composable
fun BiryaniCardImagesRow(pagerState: PagerState, biryaniItem: Biryani,context: Context) {
    HorizontalPager(state = pagerState) { page ->

        AsyncImage(
            model = biryaniItem.images[page],
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
        )
    }
}
