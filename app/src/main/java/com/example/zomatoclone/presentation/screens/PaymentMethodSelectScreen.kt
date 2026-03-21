package com.example.zomatoclone.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import com.example.zomatoclone.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.zomatoclone.presentation.ViewModels.ZomViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


private val Background = Color(0xFFF5F6FA)
private val Surface = Color(0xFFFFFFFF)
private val Primary = Color(0xFF4F46E5)   // indigo
private val PrimaryLight = Color(0xFFEEF2FF)
private val TextPrimary = Color(0xFF1E1B4B)
private val TextSecondary = Color(0xFF6B7280)
private val BorderDefault = Color(0xFFE5E7EB)
private val BorderSelected = Color(0xFF4F46E5)

data class PaymentOption(val id: Int, val label: String, val icon: Int)

@Composable
fun PaymentMethodSelectScreen(
    navController: NavController
) {
    val viewModel: ZomViewModel = hiltViewModel()
    val options = listOf(
        PaymentOption(0, "UPI / Google Pay", R.drawable.googlepay),
        PaymentOption(1, "Cash On Delivery", R.drawable.van),
        PaymentOption(2, "Apple Pay", R.drawable.applepay),
        PaymentOption(3, "Credit / Debit Card", R.drawable.card),
    )
    var selectedId = viewModel.getPaymentOption.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Payment",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary,
        )
        Text(
            text = "Choose how you'd like to pay",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.padding(top = 4.dp, bottom = 28.dp),
        )


        options.forEach { option ->
            PaymentCard(
                option = option,
                selected = selectedId == option.id,
                onClick = { viewModel.changePaymentOption(option.id) },
            )
            Spacer(modifier = Modifier.height(12.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.popBackStack()
            },
            enabled = selectedId != -1,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                disabledContainerColor = BorderDefault,
            ),
        ) {
            Text(
                text = "Continue",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
    }
}


@Composable
fun PaymentCard(
    option: PaymentOption,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) BorderSelected else BorderDefault,
        animationSpec = tween(200), label = "border"
    )
    val bgColor by animateColorAsState(
        targetValue = if (selected) PrimaryLight else Surface,
        animationSpec = tween(200), label = "bg"
    )
    val elevation by animateDpAsState(
        targetValue = if (selected) 6.dp else 1.dp,
        animationSpec = tween(200), label = "elevation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp),
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (selected) Color.White else Color(0xFFF3F4F6)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(option.icon),
                    contentDescription = option.label,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp),
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = option.label,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = if (selected) TextPrimary else TextSecondary,
                )
                if (selected) {
                    Text(
                        text = "Selected",
                        fontSize = 12.sp,
                        color = Primary,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .border(
                        width = 2.dp,
                        color = if (selected) Primary else BorderDefault,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Primary),
                    )
                }
            }
        }
    }
}