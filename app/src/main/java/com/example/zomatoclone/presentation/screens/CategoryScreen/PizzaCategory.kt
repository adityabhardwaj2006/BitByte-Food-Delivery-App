package com.example.zomatoclone.presentation.screens.CategoryScreen

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.zomatoclone.data.models.AllCategoryItem
import com.example.zomatoclone.data.models.PizzaInfo
import com.example.zomatoclone.presentation.ViewModels.AddToCartViewModel
import com.example.zomatoclone.presentation.ViewModels.PizzaViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.components.shimmerEffect
import com.example.zomatoclone.presentation.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import com.example.zomatoclone.R
import com.example.zomatoclone.data.UserDataStore

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PizzaCategory(navController: NavController,
                  viewModel: PizzaViewModel,
                  cartViewModel: AddToCartViewModel,
                  sharedViewModel: SharedViewModel){

    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val isVeg = dataStore.getVegToggle().collectAsState(initial = null).value
    var pizzaList by remember{mutableStateOf<List<PizzaInfo>?>(null)}
    val lazyListState = rememberLazyListState()
    LaunchedEffect(isVeg) {
        val data = viewModel.getPizzaInfo()
        pizzaList = when(isVeg){
            true -> data.filter { it.veg == true}
            false -> data
            null -> data
        }
    }
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
                if(pizzaList==null){
                    repeat(5){
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .shimmerEffect())
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }else{
                    pizzaList?.forEach { item->
                        HomeScreenCardPizza(navController = navController, item ,cartViewModel,LocalContext.current
                        ,sharedViewModel)

                        Spacer(modifier = Modifier.height(16.dp),)
                    }
                }



            }
        }
    }
}
@Composable
fun Category(
    foodimagebackground1: Int,
    textblackone1: String,
    cardtextname1: String,
    timingtext1: String,
    timerimage1: Int,

    foodimagebackground2: Int,
    textblackone2: String,
    cardtextname2: String,
    timingtext2: String,
    timerimage2: Int
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .background(Color.White)
    ) {

        // ---------- First Card ----------
        Card(
            modifier = Modifier
                .width(140.dp)
                .height(100.dp)
                .padding(horizontal = 2.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(id = foodimagebackground1),
                    contentDescription = "Food Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = textblackone1,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 8.dp)
                        .background(Color.Black.copy(alpha = 0.80f)),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    painter = painterResource(id = R.drawable.outline_bookmark_24),
                    contentDescription = "bookmark",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = Color.White
                )
            }
        }

        Column {
            Text(
                text = cardtextname1,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = timerimage1),
                    contentDescription = "timer",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Green
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = timingtext1,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))


        Card(
            modifier = Modifier
                .width(140.dp)
                .height(100.dp)
                .padding(horizontal = 2.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {

                Image(
                    painter = painterResource(id = foodimagebackground2),
                    contentDescription = "Food Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = textblackone2,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 8.dp)
                        .background(Color.Black.copy(alpha = 0.80f)),
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Icon(
                    painter = painterResource(id = R.drawable.outline_bookmark_24),
                    contentDescription = "bookmark",
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    tint = Color.White
                )
            }
        }

        Column {
            Text(
                text = cardtextname2,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.padding(start = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = timerimage2),
                    contentDescription = "timer",
                    modifier = Modifier.size(14.dp),
                    tint = Color.Green
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = timingtext2,
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }
    }
}

@Composable
fun HomeScreenCardPizza(
    navController: NavController,
    pizzaItem : PizzaInfo,
    cartViewModel: AddToCartViewModel,
    context : Context,
    sharedViewModel: SharedViewModel
) {
    val allCategoryItem = AllCategoryItem(
        category = "",
        description = pizzaItem.description,
        distance = pizzaItem.distance,
        id = pizzaItem.id,
        images = pizzaItem.images,
        name = pizzaItem.name,
        price = pizzaItem.price,
        rating = pizzaItem.rating,
        restaurant = pizzaItem.restaurant,
        time = pizzaItem.time,
        veg = pizzaItem.veg
    )
    val pager = rememberPagerState(0){pizzaItem.images.size}
    val scope = rememberCoroutineScope()
    LaunchedEffect(pager) {
        while(isActive){
            delay(2500)
            scope.launch {
                val newPager = (pager.currentPage + 1 )%pizzaItem.images.size
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
                description = pizzaItem.description,
                distance = pizzaItem.distance,
                id = pizzaItem.id,
                images = pizzaItem.images,
                name = pizzaItem.name
                ,
                price = pizzaItem.price,
                rating = pizzaItem.rating,
                restaurant = pizzaItem.restaurant,
                time = pizzaItem.time,
                veg = pizzaItem.veg
            )
            navController.navigate(Routes.ParticularCardScreen)
        }
    ) {
        Column {
            Box(modifier = Modifier.fillMaxSize()) {

                CardImagesRow(pager,pizzaItem,context)

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                    , verticalAlignment = Alignment.CenterVertically
                ) {
                    PriceCard(pizzaItem.name, pizzaItem.price.toString())




                }

                PageCount(pager)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 192.dp)
                        .height(120.dp)
                ) {
                    SmallDetailCard(pizzaItem)
                    DetailCard(pizzaItem)
                }
            }
        }
    }

}



@Composable
fun CardImagesRow(pagerState: PagerState,pizzaItem: PizzaInfo,context: Context) {
    HorizontalPager(state = pagerState) { page ->

        AsyncImage(
            model = pizzaItem.images[page],
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
        )
    }
}

@Composable
fun PriceCard(name: String, price: String) {
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
fun SmallDetailCard(pizzaItem: PizzaInfo) {
    val timedText = remember { mutableStateOf(pizzaItem.time) }
    val distance = remember { mutableStateOf(pizzaItem.distance) }
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

            Text(
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
fun DetailCard(pizza: PizzaInfo) {

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
                    text = pizza.name,
                    color = Color.Black,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis

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
                            text = pizza.rating,
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

                Text(
                    text = pizza.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showPizza(){
    HomeScreenCardPizza(
        rememberNavController(),
        PizzaInfo(
            "Chezzy pizza",
            "2 km ",
            1,
            price = 200,
            rating = "4",
            restaurant = "Pizza Hut",
            time = "10",
            veg = true,
            images = listOf<String>("R.drawable.pizza_image", "R.drawable.pizza_image",),
            name = "Pizza"

        ), hiltViewModel(),
        context = LocalContext.current,
        hiltViewModel()
    )
}
