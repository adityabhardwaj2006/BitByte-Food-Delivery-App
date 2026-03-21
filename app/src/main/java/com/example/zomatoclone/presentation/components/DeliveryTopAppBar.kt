package com.example.zomatoclone.presentation.components
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.presentation.navigation.Routes
import com.example.zomatoclone.R
import com.example.zomatoclone.data.UserDataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDeliveryScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {

    val viewModel: ZomViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
            viewModel.fetchUserProfile()
        }
    }

    val profile by viewModel.userProfile.collectAsState()



    val userName by viewModel.userName.collectAsState(initial = "")
    val address by viewModel.address.collectAsState(initial = "")
    LaunchedEffect(profile) {
        Log.d("ZOM_UI", "Profile value: $profile")
    }

    LaunchedEffect(address) {
        Log.d("ZOM_UI", "Address value: $address")
    }

    LaunchedEffect(userName) {
        Log.d("ZOM_UI", "Username value: $userName")
    }

    TopAppBar(
        modifier = Modifier.padding(horizontal = 4.dp),

        title = {
            Column {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Home",
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray
                    )
                }

                if (address == "") {

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier
                            .width(180.dp)
                            .height(16.dp)
                            .shimmerEffect()
                    ) {}

                } else {

                    Text(
                        text = address,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray,
                        fontSize = 15.sp
                    )
                }
            }
        },

        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.locationdeliveryscreen),
                contentDescription = "Location",
                modifier = Modifier.size(35.dp),
                tint = Color.Red
            )
        },

        actions = {

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clickable {
                        navController.navigate(Routes.ProfileScreen)
                    }
                    .background(Color.LightGray, CircleShape),

                contentAlignment = Alignment.Center
            ) {

                if (userName == "") {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .shimmerEffect()
                    )

                } else {

                    Text(
                        text = userName.firstOrNull()?.toString() ?: "G",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Blue
                    )
                }
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),

        scrollBehavior = scrollBehavior
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun showTopBar(){
        TopAppBarDeliveryScreen(scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),rememberNavController())
}
