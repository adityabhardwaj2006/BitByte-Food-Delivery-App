package com.example.zomatoclone

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import com.example.zomatoclone.presentation.navigation.App
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import androidx.compose.runtime.snapshotFlow
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.zomatoclone.ui.theme.SplashScreen.Splashscreen
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.DelicateCoroutinesApi

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition { false }
        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {

            var showSplash by remember { mutableStateOf(true) }

            if (showSplash) {

                Splashscreen {
                    showSplash = false
                }

            } else {

                val listState = rememberLazyListState()

                var isVisible by remember { mutableStateOf(true) }
                var lastIndex by remember { mutableStateOf(0) }
                var lastScrollOffset by remember { mutableStateOf(0) }

                LaunchedEffect(listState) {

                    snapshotFlow {
                        listState.firstVisibleItemIndex to
                                listState.firstVisibleItemScrollOffset
                    }
                        .distinctUntilChanged()
                        .collect { (index, offset) ->

                            if (index > lastIndex ||
                                (index == lastIndex && offset > lastScrollOffset + 50)
                            ) {
                                isVisible = false
                            }

                            else if (index < lastIndex ||
                                (index == lastIndex && offset < lastScrollOffset - 50)
                            ) {
                                isVisible = true
                            }

                            lastIndex = index
                            lastScrollOffset = offset
                        }
                }

                App(
                    isVisible = isVisible,
                    listState = listState
                )
            }
        }
    }

    override fun onPaymentSuccess(p0: String?) {
        PaymentState.isSuccess.value = true
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        PaymentState.isSuccess.value = false
    }
}
