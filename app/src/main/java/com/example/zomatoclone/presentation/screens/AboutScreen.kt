package com.example.zomatoclone.presentation.screens

import android.R.attr.versionCode
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.BuildConfig

@Composable
fun AboutScreen() {
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
    val versionName = packageInfo.versionName

    Scaffold(


    ) { padding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔹 App Logo / Name
            Text(
                text = "BitByte",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Version $versionName ($versionCode)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            Divider()

            // 🔹 About App
            SectionTitle("About App")
            SectionText(
                "BitByte is a modern food delivery app where users can browse items, add them to cart, and place orders seamlessly. " +
                        "Cart data is stored locally using Room, while authentication and order history are managed using Firebase."
            )

            // 🔹 Developer
            SectionTitle("Developer")
            SectionText("Aditya Bhardwaj")

            ClickableTextItem(
                label = "Email",
                value = "adityabhardwaj426q@gmail.com"
            ) {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:adityabhardwaj426q@gmail.com")
                }
                context.startActivity(intent)
            }

            ClickableTextItem(
                label = "LinkedIn",
                value = "View Profile"
            ) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/aditya-bhardwaj-06a98030b/")
                )
                context.startActivity(intent)
            }

            // 🔹 Tech Stack
            SectionTitle("Tech Stack")
            BulletText("Jetpack Compose")
            BulletText("MVVM Architecture")
            BulletText("Clean Architecture")
            BulletText("Firebase Authentication")
            BulletText("Firebase Firestore")
            BulletText("Room Database")
            BulletText("DataStore")
            BulletText("Kotlin Coroutines & Flow")

            // 🔹 Libraries
            SectionTitle("Libraries Used")
            BulletText("Retrofit")
            BulletText("Hilt (Dagger)")
            BulletText("Coil (AsyncImage)")
            BulletText("Lottie Animations")

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 6.dp)
    )
}

@Composable
fun SectionText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.DarkGray
    )
}

@Composable
fun BulletText(text: String) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(start = 8.dp, top = 2.dp)
    )
}

@Composable
fun ClickableTextItem(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.primary
        )
    }
}