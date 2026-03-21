package com.example.zomatoclone.presentation.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zomatoclone.R

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun DiningScreenComponents() {
    RestaurantsNearMeCard()
    Spacer(modifier = Modifier.height(40.dp))
    Title()
    Spacer(modifier = Modifier.height(40.dp))
    RestaurantLazyRow()
    Spacer(modifier = Modifier.height(40.dp))
    EditorChoiceText()
    TrendingSpotsLazyRow()
}

@Composable
fun TrendingSpotsLazyRow() {
    LazyRow(
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            TrendingSpotCard(
                imageId = when (index) {
                    0 -> R.drawable.restaurant1
                    1 -> R.drawable.restaurant2
                    2 -> R.drawable.restaurant3
                    3 -> R.drawable.restaurant4
                    4 -> R.drawable.restaurant5
                    else -> R.drawable.restaurant6
                },
                locationName = when (index) {
                    0 -> "Top Trending Spots"
                    1 -> "Best Rooftop Places"
                    2 -> "New Places"
                    3 -> "Iftar Special"
                    4 -> "Romantic"
                    else -> "Trending Spot"
                }
            )
        }
    }
}

@Composable
fun TrendingSpotCard(
    imageId: Int,
    locationName: String
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .height(200.dp),
        shape = RoundedCornerShape(
            topStart = 150.dp,
            topEnd = 150.dp,
            bottomStart = 12.dp,
            bottomEnd = 12.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(imageId),
                contentDescription = "Trending Spot",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 150.dp,
                            topEnd = 150.dp,
                            bottomStart = 12.dp,
                            bottomEnd = 12.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            ) {
                Text(
                    text = locationName,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun EditorChoiceText() {
    Text(
        text = "EDITOR'S CHOICE",
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.SansSerif
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RestaurantLazyRow() {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(5) { index ->
            RestaurantCard(
                imageId = when (index) {
                    0 -> R.drawable.hotspot1
                    1 -> R.drawable.hotspots2
                    2 -> R.drawable.hotspots3
                    3 -> R.drawable.hotspots4
                    4 -> R.drawable.hotspots5
                    else -> R.drawable.restaurant6
                },
                locationName = when (index) {
                    0 -> "High Street"
                    1 -> "Capital Building"
                    2 -> "Crowne Plaza"
                    3 -> "Seaside View"
                    4 -> "Phoenix Marketcity"
                    else -> "Location"
                }
            )
        }
    }
}

@Composable
fun RestaurantCard(
    imageId: Int,
    locationName: String
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(100.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageId),
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                contentDescription = "Location Background"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            ) {
                Text(
                    text = locationName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier = Modifier
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun Title() {
    Text(
        text = "HOTSPOTS UNDER 10 KM",
        style = TextStyle(
            fontSize = 14.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Normal,
            letterSpacing = 2.sp,
            fontFamily = FontFamily.SansSerif
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun RestaurantsNearMeCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.diningsearchbanner),
                contentDescription = "Restaurants Near Me",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}
