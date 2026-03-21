package com.example.zomatoclone.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.R
import com.example.zomatoclone.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDiningScreen(
    navController: NavController
) {
    val viewModel: ZomViewModel = hiltViewModel()

    val profile by viewModel.userProfile.collectAsState()
    val address by viewModel.address.collectAsState(initial = "")



    // Fade-out
    TopAppBar(
        title = {
            Column() {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Home",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.down_arrow),
                        contentDescription = "Down Arrow",
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 6.dp),
                        tint = Color.Black
                    )
                }

                Text(
                    text = address ,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 15.sp
                )
            }
        },

        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.locationdiningscreen),
                contentDescription = "Location",
                modifier = Modifier.size(30.dp),
                tint = Color.Black
            )
        },

        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Routes.ProfileScreen)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Profile",
                    tint = Color.Black
                )
            }
        },

        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),

        modifier = Modifier.fillMaxWidth()
    )
}
 
