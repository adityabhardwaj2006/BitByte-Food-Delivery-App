package com.example.zomatoclone.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.zomatoclone.R
import com.example.zomatoclone.data.UserDataStore
import com.example.zomatoclone.data.models.AllCategoryItem
import com.example.zomatoclone.data.models.QuickScreenItems
import com.example.zomatoclone.presentation.ViewModels.QuickViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.presentation.components.HomeScreenCard
import com.example.zomatoclone.presentation.components.TopAppBarDiningScreen
import com.example.zomatoclone.presentation.utils.BottomSheetToAddProduct


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickScreen(
    navController: NavController,
    listState: LazyListState,
    sharedViewModel: SharedViewModel,
    viewModel: QuickViewModel
) {
    val zomViewModel: ZomViewModel = hiltViewModel()

    zomViewModel.fetchUserProfile()
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val isVeg = dataStore.getVegToggle().collectAsState(initial = null).value

    var itemList by remember { mutableStateOf<List<QuickScreenItems>?>(null) }
    LaunchedEffect(isVeg) {
        val data = viewModel.getQuickDeliveryItem()

        itemList = when (isVeg) {
            true -> data.filter { it.veg == true }
            false -> data
            null -> data
        }
    }

    val showBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            TopAppBarDiningScreen(
                navController = navController
            )
        }
    ) { innerPadding ->

        LazyColumn(
            contentPadding = PaddingValues(vertical = 0.dp),
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {

            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.quickbanner),
                        contentDescription = "Quick Screen",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 15.dp,
                                    bottomEnd = 15.dp
                                )
                            )
                    )
                }
            }

            item {
                Column {
                    Text(
                        text = "Quick Picks",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 12.dp)
                    )

                    Text(
                        text = "Swipe to explore • Tap ADD to order",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(itemList ?: emptyList()) { item ->
                            QuickScreenComponents(
                                navController = navController,
                                onAddClick = {
                                    showBottomSheet.value = true
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
                                },
                                item
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Swipe to explore • Tap ADD to order",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )
                HomeScreenCard(navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (showBottomSheet.value) {
            BottomSheetToAddProduct(
                onDismiss = { showBottomSheet.value = false },
                navController = navController,
                sharedViewModel

            )
        }
    }
}


@Composable
fun QuickScreenComponents(
    navController: NavController, onAddClick: () -> Unit,
    items: QuickScreenItems
) {
    var productName by remember { mutableStateOf(items.name) }
    var rating by remember { mutableStateOf(items.rating) }
    var price by remember { mutableStateOf("₹ ${items.price}") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.width(200.dp)) {

                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(start = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(

                        painter = if (items.veg == true) painterResource(
                            id = R.drawable.veg_icon
                        )
                        else {
                            painterResource(id = R.drawable.nonveg)
                        },
                        contentDescription = null,
                        modifier = Modifier.size(17.dp),
                        tint = Color.Unspecified

                    )

                    Card(
                        modifier = Modifier.padding(start = 8.dp),
                        shape = RoundedCornerShape(5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colorResource(R.color.purple_200).copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = "Bestseller",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            color = if (items.veg) {
                                Color(0xFF227425)
                            } else {
                                Color(0xFF9B1515)
                            },
                            fontSize = 11.sp
                        )
                    }
                }

                Text(
                    text = productName,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3F51B5),
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Card(
                    modifier = Modifier.size(width = 50.dp, height = 22.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF409944)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = rating,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Icon(
                            painter = painterResource(R.drawable.star),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.White
                        )
                    }
                }

                Text(
                    text = price,
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFF000000),
                    fontSize = 16.sp
                )
            }

            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    model = items.images[0],
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(15.dp))
                )

                Card(
                    onClick = { onAddClick() },
                    modifier = Modifier
                        .padding(top = 120.dp)
                        .size(width = 100.dp, height = 36.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFF388E3C)
                    )
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ADD",
                            modifier = Modifier.padding(start = 20.dp),
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Icon(
                            painter = painterResource(R.drawable.baseline_add_24),
                            contentDescription = "Add item",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

