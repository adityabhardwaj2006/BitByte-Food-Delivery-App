package com.example.zomatoclone.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun SignUpScreenShimmer() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = Modifier
                .width(80.dp)
                .padding(start = 8.dp, bottom = 5.dp, top = 8.dp)
                .shimmerEffect()
                .height(20.dp)
        ){}

        Row(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 5.dp, top = 8.dp)
                .shimmerEffect()
                .width(270.dp)
                .height(20.dp),
            horizontalArrangement = Arrangement.Start
        ) {

        }
        Row(
            modifier = Modifier
                .padding(start = 8.dp, bottom = 8.dp, top = 8.dp)
                .shimmerEffect()
                .width(60.dp)
                .height(20.dp),
            horizontalArrangement = Arrangement.Start
        ) {

        }

        HomeScreenCardShimmer()
        Spacer(modifier = Modifier.height(16.dp))

        HomeScreenCardShimmer()
        Spacer(modifier = Modifier.height(16.dp))

        HomeScreenCardShimmer()
        Spacer(modifier = Modifier.height(16.dp))

        HomeScreenCardShimmer()
        Spacer(modifier = Modifier.height(16.dp))

        HomeScreenCardShimmer()
        Spacer(modifier = Modifier.height(16.dp))
    }
}


fun Modifier.shimmerEffect(): Modifier = composed {

    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0),
    )

    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "")

    val translateAnim by transition.animateFloat(
        initialValue = -size.width.toFloat(),
        targetValue = size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim, 0f),
        end = Offset(translateAnim + size.width, size.height.toFloat())
    )

    background(brush)
        .onGloballyPositioned {
            size = it.size
        }
}
@Composable
fun HomeScreenCardShimmer() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(14.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
    }
}