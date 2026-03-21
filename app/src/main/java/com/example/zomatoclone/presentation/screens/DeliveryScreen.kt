package com.example.zomatoclone.presentation.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zomatoclone.presentation.ViewModels.AddToCartViewModel
import com.example.zomatoclone.presentation.ViewModels.AllCategoryViewModel
import com.example.zomatoclone.presentation.ViewModels.BiryaniViewModel
import com.example.zomatoclone.presentation.ViewModels.BurgerViewModel
import com.example.zomatoclone.presentation.ViewModels.ChineseViewModel
import com.example.zomatoclone.presentation.ViewModels.PizzaViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.presentation.components.FoodCategoryTabs
import com.example.zomatoclone.presentation.components.TopAppBarDeliveryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.AllCategoryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.BiryaniCategoryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.BurgerCategoryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.ChineseCategoryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.PizzaCategory
import com.example.zomatoclone.R
import com.example.zomatoclone.presentation.ViewModels.RollsViewModel
import com.example.zomatoclone.presentation.ViewModels.ShortCutViewModel
import com.example.zomatoclone.presentation.screens.CategoryScreen.RollsCategoryScreen
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.zomatoclone.presentation.ViewModels.IceCreamViewModel
import com.example.zomatoclone.presentation.ViewModels.PastaViewModel
import com.example.zomatoclone.presentation.ViewModels.SweetsViewModel
import com.example.zomatoclone.presentation.screens.CategoryScreen.IceCreamCategoryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.PastaCategoryScreen
import com.example.zomatoclone.presentation.screens.CategoryScreen.SweetsCategoryScreen


@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun DeliveryScreen(
    navController: NavController,
    listState: LazyListState,
    sharedViewModel: SharedViewModel,
    shortCutViewModel: ShortCutViewModel

) {
    val pastaViewModel: PastaViewModel = hiltViewModel()
    val iceCreamViewModel: IceCreamViewModel = hiltViewModel()
    val sweetsViewModel: SweetsViewModel = hiltViewModel()
    val pizzaViewModel: PizzaViewModel = hiltViewModel()
    val cartViewModel: AddToCartViewModel = hiltViewModel()
    val chineseViewModel: ChineseViewModel = hiltViewModel()
    val allCatViewModel: AllCategoryViewModel = hiltViewModel()
    val biryaniViewModel: BiryaniViewModel = hiltViewModel()
    val burgerViewModel: BurgerViewModel = hiltViewModel()
    val rollsViewModel: RollsViewModel = hiltViewModel()

    val viewModel: ZomViewModel = hiltViewModel()


    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    val tabName = shortCutViewModel.tabName.collectAsState().value

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {

        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.fetchUserProfile()
        }

    }

    LaunchedEffect(tabName) {
        tabName?.let {
            selectedTabIndex = when (tabName) {

                "All" -> 0
                "Pizza" -> 1
                "Chinese" -> 2
                "Burger" -> 3
                "Biryani" -> 4
                "Ice Cream" -> 8
                "Sweets" -> 5
                "Pasta" -> 6
                "Rolls" -> 7
                else -> {
                    0
                }
            }
        }
    }
    Log.d("VM_CHECK", shortCutViewModel.hashCode().toString())

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars),
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .background(Color.White)
            ) {
                TopAppBarDeliveryScreen(scrollBehavior, navController)
                Spacer(modifier = Modifier.height(3.dp))
                DeliveryScreenSearchBar(navController)
            }
        }
    ) { innerPadding ->

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(vertical = 0.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {

            stickyHeader {
                FoodCategoryTabs(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    selectedTabIndex = selectedTabIndex,
                    onTabSelected = { selectedTabIndex = it }
                )
            }

            item {
                AnimatedContent(
                    targetState = selectedTabIndex,
                    transitionSpec = {
                        val direction = if (targetState > initialState) {
                            AnimatedContentTransitionScope.SlideDirection.Left
                        } else {
                            AnimatedContentTransitionScope.SlideDirection.Right
                        }
                        slideIntoContainer(direction, tween(500)) + fadeIn() togetherWith
                                slideOutOfContainer(direction, tween(500)) + fadeOut()
                    },
                    label = "SlideTabTransition"
                ) { index ->
                    when (index) {
                        0 -> AllCategoryScreen(
                            navController,
                            allCatViewModel,
                            cartViewModel,
                            sharedViewModel
                        )

                        1 -> PizzaCategory(
                            navController,
                            pizzaViewModel,
                            cartViewModel,
                            sharedViewModel
                        )

                        2 -> ChineseCategoryScreen(
                            navController,
                            chineseViewModel,
                            cartViewModel,
                            sharedViewModel
                        )

                        3 -> BurgerCategoryScreen(
                            navController,
                            burgerViewModel,
                            cartViewModel,
                            sharedViewModel
                        )

                        4 -> BiryaniCategoryScreen(
                            navController,
                            biryaniViewModel,
                            cartViewModel,
                            sharedViewModel
                        )
                        //Sweets
                        5 -> SweetsCategoryScreen(
                            navController,
                            sweetsViewModel,
                            cartViewModel,
                            sharedViewModel,
                        )
                        //Pasta
                        6 -> PastaCategoryScreen(
                            navController,
                            pastaViewModel,
                            cartViewModel,
                            sharedViewModel,

                            )

                        7 -> RollsCategoryScreen(
                            navController,
                            rollsViewModel,
                            cartViewModel,
                            sharedViewModel,

                            )
                        //Ice cream
                        8 -> IceCreamCategoryScreen(
                            navController,
                            iceCreamViewModel,
                            cartViewModel,
                            sharedViewModel,

                            )

                        else -> AllCategoryScreen(
                            navController,
                            allCatViewModel,
                            cartViewModel,
                            sharedViewModel
                        )
                    }
                }
            }
        }
    }
}
