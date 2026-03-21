package com.example.zomatoclone.presentation.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import androidx.navigation.navigation
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.screens.*
import com.google.firebase.auth.FirebaseAuth
import com.example.zomatoclone.R
import com.example.zomatoclone.presentation.ViewModels.QuickViewModel
import com.example.zomatoclone.presentation.ViewModels.SearchViewModel
import com.example.zomatoclone.presentation.ViewModels.ShortCutViewModel

data class BottomNavItem(
    val title: String,
    val icon: Painter,
    val route: Routes
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    isVisible: Boolean,
    listState: LazyListState
) {

    val firebaseAuth = FirebaseAuth.getInstance()
    val navController = rememberNavController()
    val searchViewModel : SearchViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Routes.DeliveryScreen::class.qualifiedName,
        Routes.QuickScreen::class.qualifiedName,
        Routes.CartScreen::class.qualifiedName
    )

    val bottomItems = listOf(
        BottomNavItem("Delivery", painterResource(R.drawable.delivery_cart), Routes.DeliveryScreen),
        BottomNavItem("Quick", painterResource(R.drawable.quick_icon), Routes.QuickScreen),
        BottomNavItem("Cart", rememberVectorPainter(Icons.Default.ShoppingCart), Routes.CartScreen)
    )



    val startDestination = if(firebaseAuth.currentUser?.uid!=null){
        SubNavigation.MainHomeScreen
    }else SubNavigation.LoginSignUpScreen

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar&&isVisible,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    bottomItems.forEachIndexed { index, item ->
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate(item.route::class.qualifiedName!!){
                                        popUpTo(SubNavigation.MainHomeScreen){
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                painter = item.icon,
                                modifier = Modifier.size(26.dp),
                                contentDescription = item.title,
                                tint = if (currentRoute == item.route::class.qualifiedName)
                                    colorResource(R.color.purple_500)
                                else Color.Gray
                            )
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                color = if (currentRoute == item.route::class.qualifiedName)
                                    colorResource(R.color.purple_500)
                                else Color.Gray
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()/4
                )
        ) {

            // AUTH GRAPH
            navigation<SubNavigation.LoginSignUpScreen>(
                startDestination = Routes.LoginScreen
            ) {
                composable<Routes.LoginScreen> {
                    LoginScreen(navController)
                }
                composable<Routes.SignUpScreen> {
                    SignUpScreen(navController)
                }
            }

            // MAIN GRAPH
            navigation<SubNavigation.MainHomeScreen>(
                startDestination = Routes.DeliveryScreen
            ) {

                composable<Routes.PaymentMethodSelectScreen>{
                    PaymentMethodSelectScreen(navController)
                }

                composable<Routes.DeliveryScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                    val shortCutViewModel: ShortCutViewModel = hiltViewModel(parentEntry)

                    DeliveryScreen(navController, listState, sharedViewModel, shortCutViewModel)
                }

                composable<Routes.QuickScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }
                    val viewModel : QuickViewModel = hiltViewModel()

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    QuickScreen(navController, listState, sharedViewModel,viewModel)
                }



                composable<Routes.CartScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    CartScreen(navController, listState, sharedViewModel)
                }

                composable<Routes.ProfileScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    ProfileScreen(navController)
                }

                composable<Routes.ParticularCardScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    ParticularCardScreen(navController, sharedViewModel)
                }

                composable<Routes.FinalCheckoutScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    FinalCheckoutScreen(navController, listState, sharedViewModel)
                }

                composable<Routes.SearchBarScreen> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)
                    val shortCutViewModel: ShortCutViewModel = hiltViewModel(parentEntry)

                    SearchBarScreen(navController, sharedViewModel, searchViewModel, shortCutViewModel)
                }

                composable<Routes.PastOrders> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    PastOrders(navController, sharedViewModel)
                }

                composable<Routes.YourFeedback> { backStackEntry ->

                    val parentEntry = remember(backStackEntry) {
                        navController.getBackStackEntry(SubNavigation.MainHomeScreen)
                    }

                    val sharedViewModel: SharedViewModel = hiltViewModel(parentEntry)

                    YourFeedback(navController)
                }

                composable<Routes.AddressBook> {
                    AddressBook()
                }

                composable<Routes.AboutScreen> {
                    AboutScreen()
                }
            }
        }
    }
}
