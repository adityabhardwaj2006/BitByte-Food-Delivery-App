package com.example.zomatoclone.ui.theme.SplashScreen

import android.R.attr.scaleY
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zomatoclone.R
import kotlinx.coroutines.delay

@Composable
fun Splashscreen(onNavigate: () -> Unit) {

    var startAnimation by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }


    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.6f,
        animationSpec = tween(durationMillis = 1000),
        label = "scaleAnim"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alphaAnim"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(1200)

        showTagline = true
        delay(1800)

        onNavigate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6A1B9A)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            Icon(
                painter = painterResource(R.drawable.bitbyte),
                contentDescription = "App Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    },
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(16.dp))


            AnimatedVisibility(
                visible = showTagline,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Text(
                    text = "Delivering Happiness, One Bite at a Time",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}
