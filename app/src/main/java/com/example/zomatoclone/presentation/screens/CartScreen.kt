package com.example.zomatoclone.presentation.screens

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel

@Composable
fun CartScreen(
    navController: NavController,
    listState: LazyListState,
    sharedViewModel: SharedViewModel
) {
    FinalCheckoutScreen(navController, listState, sharedViewModel)
}