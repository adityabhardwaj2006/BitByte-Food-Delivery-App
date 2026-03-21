package com.example.zomatoclone.presentation.utils

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.*
import com.example.zomatoclone.R

@Composable
fun OrderPlacedDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "colorTransition")

    val animatedColor by transition.animateColor(
        initialValue = colorResource(R.color.purple_200),
        targetValue = colorResource(R.color.purple_500),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animatedColor"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                HeaderImage(modifier = Modifier.height(150.dp))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Order Placed!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = animatedColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Your payment was successful.\nA receipt for this purchase has\nbeen sent to your mail.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    style = TextStyle(lineHeight = 18.sp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.purple_500),
                        contentColor = Color.White
                    )
                ) {

                    Text(
                        text = "Go Back",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun HeaderImage(modifier: Modifier) {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.order_placed)
    )

    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ShowThis() {

    MaterialTheme {
        OrderPlacedDialog(
            onDismiss = {},
            onConfirm = {}
        )
    }
}

