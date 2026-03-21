package com.example.zomatoclone.presentation.screens
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.zomatoclone.R

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.zomatoclone.presentation.navigation.Routes
import java.util.Locale

@Composable
fun DeliveryScreenSearchBar(navController: NavController) {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(2.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .clickable {
                navController.navigate(Routes.SearchBarScreen)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.width(16.dp))

        Icon(
            imageVector = Icons.Default.Search,
            tint = colorResource(R.color.purple_500),
            contentDescription = "Search Icon",
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicTextField(
            value = query,
            onValueChange = { query = it },
            enabled = false,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = Color.Gray
            ),
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Restaurant name or a dish...",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                innerTextField()
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            painter = painterResource(R.drawable.mic),
            contentDescription = "Voice Search",
            tint = colorResource(R.color.purple_500),
            modifier = Modifier.size(24.dp)
                .clickable(
                    onClick = {
                        navController.navigate(Routes.SearchBarScreen)
                    }
                )
        )

        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable

fun VegModeToggle(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Column ( horizontalAlignment = Alignment. CenterHorizontally,
    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
    ){
        Text(text = "VEG",
    fontWeight = FontWeight. Bold,
    fontSize = 14. sp,
    color = Color.Black,
    textAlign = TextAlign.Center,
    )
        Text(text = "MODE",
            fontWeight = FontWeight. Bold,
            fontSize = 10. sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
        CustomSwitch(isChecked, onCheckedChange)
    }
}


@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 38.dp, height = 18.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(if (checked) Color(0xFF008000) else Color.LightGray)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .offset(x = if (checked) 22.dp else 2.dp)
                .clip(CircleShape)
                .shadow(elevation = 8.dp, shape = CircleShape)
                .background(Color.White)
        )
    }
}

@Preview
@Composable
fun see(){
    var navCont = rememberNavController()
    DeliveryScreenSearchBar(navCont)
}

