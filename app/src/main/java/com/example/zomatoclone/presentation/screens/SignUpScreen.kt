package com.example.zomatoclone.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zomatoclone.R
import com.example.zomatoclone.data.models.UserData
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import com.example.zomatoclone.presentation.navigation.Routes
import com.example.zomatoclone.presentation.navigation.SubNavigation

import com.example.zomatoclone.presentation.components.SignUpScreenShimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: ZomViewModel = hiltViewModel()
) {

    val signUpState by viewModel._signUpState.collectAsState()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(signUpState.success) {
        if (signUpState.success) {
            navController.navigate(SubNavigation.MainHomeScreen) {
                popUpTo(SubNavigation.LoginSignUpScreen) {
                    inclusive = true
                }
            }
        }
    }



    when {
        signUpState.loading -> {
            SignUpScreenShimmer()
        }

        signUpState.error != null -> {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material.Icon(
                    painter = painterResource(R.drawable.error),
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(Modifier.height(4.dp))
                androidx.compose.material.Text(
                    text = signUpState.error.toString(),
                    fontSize = 18.sp,
                    color = Color(0xFFE23744),
                    textAlign = TextAlign.Center
                )

            }
        }

        else -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {

                    animImage(
                        modifier = Modifier
                            .height(250.dp)
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(WindowInsets.statusBars.asPaddingValues())
                            .padding(top = 45.dp)
                    )


                    Card(
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.TopEnd),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.7f)
                        )
                    ) {
                        Text(
                            text = "Skip",
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                                .clickable {
                                    navController.navigate(SubNavigation.MainHomeScreen)
                                }
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {

                    // Titles
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Get Started",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp
                        )
                        Text(
                            text = "by creating a free account",
                            fontSize = 14.sp
                        )
                    }

                    // Fields
                    Column {
                        Field(name, "Full Name", R.drawable.person, false) { name = it }
                        Spacer(Modifier.height(8.dp))
                        Field(email, "Email", R.drawable.mail, false) { email = it }
                        Spacer(Modifier.height(8.dp))
                        Field(
                            address,
                            "Address",
                            R.drawable.locationdiningscreen,
                            false
                        ) { address = it }
                        Spacer(Modifier.height(8.dp))
                        Field(pass, "Password", R.drawable.padlock, true) { pass = it }
                        Spacer(Modifier.height(8.dp))
                        Field(confirm, "Confirm Password", R.drawable.padlock, true) {
                            confirm = it
                        }
                    }

                    // Terms
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(checked = checked, onCheckedChange = { checked = it })
                        Text(
                            text = "I agree to Terms & Conditions",
                            fontSize = 12.sp
                        )
                    }

                    // Button
                    Button(
                        onClick = {
                            when {
                                pass != confirm ->
                                    Toast.makeText(
                                        context,
                                        "Passwords do not match",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                name.isBlank() || email.isBlank() || address.isBlank() || pass.isBlank() ->
                                    Toast.makeText(context, "Fill all fields", Toast.LENGTH_SHORT)
                                        .show()

                                else -> viewModel.createUser(
                                    UserData(
                                        email = email,
                                        password = pass,
                                        name = name,
                                        address = address
                                    )
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE23744)
                        )
                    ) {
                        Text("Sign Up", fontSize = 18.sp, color = Color.White)
                    }

                    // Sign in
                    Row {
                        Text("Already a user? ")
                        Text(
                            "Sign in",
                            color = Color(0xFFE23744),
                            modifier = Modifier.clickable {
                                navController.navigate(Routes.LoginScreen)
                            }
                        )
                    }
                }
            }


        }
    }
}


@Composable
fun Field(
    value: String,
    placeholder: String,
    icon: Int,
    flag: Boolean,
    onValueChange: (String) -> Unit
) {

    var toggle by remember { mutableStateOf(false) }
    OutlinedTextField(
        singleLine = true,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 20.sp, fontWeight = FontWeight.Light) },
        leadingIcon = {
            Icon(
                painterResource(id = icon),
                contentDescription = "",
                modifier = Modifier
                    .height(20.dp)
                    .width(30.dp)
            )
        },
        modifier = Modifier.width(350.dp),
        visualTransformation =
            if (flag) {
                if (toggle) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                }
            } else {
                VisualTransformation.None
            },
        trailingIcon = {
            if (flag) {
                IconButton(
                    onClick = { toggle = !toggle }
                ) {
                    Icon(
                        imageVector = if (toggle)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = "Toggle password visibility"
                    )
                }

            }
        },

        )
}

