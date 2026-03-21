package com.example.zomatoclone.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.presentation.navigation.Routes
import com.example.zomatoclone.presentation.navigation.SubNavigation
import com.example.zomatoclone.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ZomViewModel = hiltViewModel()
) {
    val profile by viewModel.userProfile.collectAsState()
    val name by viewModel.userName.collectAsState(initial = "Guest")
    Scaffold(
        topBar = {

            TopAppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),

                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF2F4F7))
        ) {
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .height(140.dp)
            ) {
                UserCard(name)
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { MoneyCard() }

                item {
                    MenuCard {
                        SectionHeader("Your preferences")

                        var vegMode = viewModel.vegToggle.collectAsState()

                        MenuRow(
                            icon = painterResource(R.drawable.veg_icon),
                            title = "Veg Mode",
                            isToggle = true,
                            toggleState = vegMode.value,
                            onToggle = { viewModel.changeVegToggle(it) },
                            iconTint = Color(0xFF4CAF50),
                            navController = navController,
                            viewModel = viewModel
                        )


                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)

                        MenuRow(
                            icon = painterResource(R.drawable.rupeesymbol),
                            title = "Payment Methods",
                            navController = navController,
                            viewModel = viewModel,
                        )
                    }
                }

                item {
                    MenuCard {
                        SectionHeader("Food delivery")

                        MenuRow(
                            icon = painterResource(R.drawable.order),
                            title = "Your Orders",
                            navController = navController,
                            viewModel = viewModel
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRow(
                            icon = painterResource(R.drawable.addressbook),
                            title = "Address Book",
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }



                item {
                    MenuCard {
                        SectionHeader("More")

                        MenuRow(
                            icon = painterResource(R.drawable.feedback),
                            title = "Your Feedback",
                            navController = navController,
                            viewModel = viewModel
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRow(
                            icon = rememberVectorPainter(Icons.Default.Info),
                            title = "About",
                            navController = navController,
                            viewModel = viewModel
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)

                        MenuRow(
                            icon = painterResource(R.drawable.logout),
                            title = "Log Out",
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}


@Composable
fun MenuCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            content()
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .height(24.dp)
                .width(4.dp)
                .background(
                    color = Color(0xFF009688),
                    shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun MenuRow(
    icon: Painter,
    title: String,
    iconTint: Color = Color.Gray,
    endText: String? = null,
    isToggle: Boolean = false,
    toggleState: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    navController: NavController,
    viewModel: ZomViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isToggle) onToggle(!toggleState)
                if (title == "Payment Methods") {
                    navController.navigate(Routes.PaymentMethodSelectScreen)
                }
                if (title == "Log Out") {
                    viewModel.logOut()

                    navController.navigate(Routes.LoginScreen) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }


                }
                if (title == "Your Orders") {
                    navController.navigate(Routes.PastOrders)

                }
                if (title == "Your Feedback") {
                    navController.navigate(Routes.YourFeedback)
                }
                if (title == "Address Book") {
                    navController.navigate(Routes.AddressBook)
                }
                if (title == "About") {
                    navController.navigate(Routes.AboutScreen)
                }

            }

            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (isToggle) {
            Switch(
                checked = toggleState,
                onCheckedChange = onToggle,
                modifier = Modifier.scale(0.8f),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF2E7D32),
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    uncheckedThumbColor = Color.White,
                    disabledCheckedTrackColor = Color(0xFFBDBDBD),
                    disabledUncheckedTrackColor = Color(0xFFEDEDED)
                )
            )
        } else {
            if (endText != null) {
                Text(endText, fontSize = 14.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}


@Composable
fun MoneyCard() {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            colors = CardDefaults.cardColors(Color.White),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.payment),
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
                Column(modifier = Modifier.padding(start = 10.dp)) {
                    Text("BitByte Money", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        "₹1500",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(
                    onClick = {
                        Toast.makeText(context, "No coupons available", Toast.LENGTH_SHORT).show()
                    }
                ),
            colors = CardDefaults.cardColors(Color.White),
            shape = MaterialTheme.shapes.medium
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalOffer,
                    contentDescription = null,
                    tint = Color.Blue
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text("Your coupons", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun UserCard(name: String) {


    Card(
        modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(Color.White)
    ) {
        Column()
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF57A3F0)), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name
                            .trim()
                            .firstOrNull().toString(), modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp), fontWeight = FontWeight.Bold, fontSize = 40.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Blue
                    )

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {

                    Text(text = name, fontWeight = FontWeight.Bold, fontSize = 30.sp)

                }

            }


        }
    }

}

@Composable
fun MenuRowClone(
    icon: Painter,
    title: String,
    iconTint: Color = Color.Gray,
    endText: String? = null,
    isToggle: Boolean = false,
    toggleState: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    navController: NavController,
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isToggle) onToggle(!toggleState)
                if (title == "Log Out") {
                    navController.navigate(SubNavigation.LoginSignUpScreen) {
                        popUpTo(0) {
                            inclusive = true

                        }
                    }


                }
            }

            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (isToggle) {
            Switch(
                checked = toggleState,
                onCheckedChange = onToggle,
                modifier = Modifier.scale(0.8f),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF2E7D32),
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    uncheckedThumbColor = Color.White,
                    disabledCheckedTrackColor = Color(0xFFBDBDBD),
                    disabledUncheckedTrackColor = Color(0xFFEDEDED)
                )
            )
        } else {
            if (endText != null) {
                Text(endText, fontSize = 14.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ShowProfile() {

    val navController = rememberNavController()

    Scaffold(
        topBar = {

            TopAppBar(
                modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),

                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF2F4F7))
        ) {
            Column(
                modifier = Modifier
                    .padding(14.dp)
                    .height(140.dp)
            ) {
                UserCard("Aditya Bhardwaj")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { MoneyCard() }

                item {
                    MenuCard {
                        SectionHeader("Your preferences")

                        var vegMode by remember { mutableStateOf(true) }
                        MenuRowDummy(
                            icon = painterResource(R.drawable.veg_icon),
                            title = "Veg Mode",
                            isToggle = true,
                            toggleState = vegMode,
                            onToggle = { vegMode = it },
                            iconTint = Color(0xFF4CAF50),
                            navController = navController,
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)

                        var healthyMode by remember { mutableStateOf(false) }
                        MenuRowDummy(
                            icon = painterResource(R.drawable.outline_info_24),
                            title = "Healthy Mode",
                            isToggle = true,
                            toggleState = healthyMode,
                            onToggle = { healthyMode = it },
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)

                        MenuRowDummy(
                            icon = painterResource(R.drawable.rupeesymbol),
                            title = "Payment Methods",
                            navController = navController,
                        )
                    }
                }

                item {
                    MenuCard {
                        SectionHeader("Food delivery")

                        MenuRowDummy(
                            icon = painterResource(R.drawable.order),
                            title = "Your Orders",
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = painterResource(R.drawable.addressbook),
                            title = "Address Book",
                            navController = navController
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = painterResource(R.drawable.outline_bookmark_24),
                            title = "Your Collections",
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = painterResource(R.drawable.heart),
                            title = "Manage Recommendations",
                            navController = navController
                        )
                    }
                }

                item {
                    MenuCard {
                        SectionHeader("Dining & Experiences")

                        MenuRowDummy(
                            icon = painterResource(R.drawable.timer),
                            title = "Dining Transactions",
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = painterResource(R.drawable.gift),
                            title = "Dining Rewards",
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = painterResource(R.drawable.dining),
                            title = "Your Bookings",
                            navController = navController,
                        )
                    }
                }

                item {
                    MenuCard {
                        SectionHeader("More")

                        MenuRowDummy(
                            icon = painterResource(R.drawable.feedback),
                            title = "Your Feedback",
                            navController = navController
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = rememberVectorPainter(Icons.Default.Info),
                            title = "About",
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = rememberVectorPainter(Icons.Default.Settings),
                            title = "Settings",
                            navController = navController,
                        )
                        HorizontalDivider(color = Color.LightGray, thickness = 0.5.dp)
                        MenuRowDummy(
                            icon = painterResource(R.drawable.logout),
                            title = "Log Out",
                            navController = navController,
                        )
                    }
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun MenuRowDummy(
    icon: Painter,
    title: String,
    iconTint: Color = Color.Gray,
    endText: String? = null,
    isToggle: Boolean = false,
    toggleState: Boolean = false,
    onToggle: (Boolean) -> Unit = {},
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (isToggle) onToggle(!toggleState)
                if (title == "Log Out") {
                    navController.navigate(SubNavigation.LoginSignUpScreen) {
                        popUpTo(0) {
                            inclusive = true

                        }
                    }

                }
                if (title == "Your Orders") {


                    navController.navigate(Routes.PastOrders)


                }
                if (title == "Your Feedback") {


                    navController.navigate(Routes.YourFeedback)


                }

                if (title == "About") {
                    navController.navigate(Routes.AboutScreen)
                }
            }

            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 15.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        if (isToggle) {
            Switch(
                checked = toggleState,
                onCheckedChange = onToggle,
                modifier = Modifier.scale(0.8f),
                colors = SwitchDefaults.colors(
                    checkedTrackColor = Color(0xFF2E7D32),
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFFE0E0E0),
                    uncheckedThumbColor = Color.White,
                    disabledCheckedTrackColor = Color(0xFFBDBDBD),
                    disabledUncheckedTrackColor = Color(0xFFEDEDED)
                )
            )
        } else {
            if (endText != null) {
                Text(endText, fontSize = 14.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
