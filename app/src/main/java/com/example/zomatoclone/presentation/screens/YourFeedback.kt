package com.example.zomatoclone.presentation.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zomatoclone.presentation.navigation.Routes
import com.example.zomatoclone.presentation.navigation.SubNavigation

private val OrangeMain = Color(0xFF6A1B9A)
private val OrangeSoft = Color(0xFFFFF0EB)
private val OrangeText = Color(0xFF6A1B9A)
private val OrangeBorder = Color(0xFFFFB99A)
private val StarYellow = Color(0xFFFFC107)
private val StarEmpty = Color(0xFFE0DDD8)
private val BgPage = Color(0xFFFAF9F7)
private val BgCard = Color(0xFFFFFFFF)
private val BgInput = Color(0xFFF5F3F0)
private val TextPrimary = Color(0xFF1C1917)
private val TextSecondary = Color(0xFF78716C)
private val TextHint = Color(0xFFB5B0AA)
private val DividerColor = Color(0xFFEDE9E4)
private val SuccessGreen = Color(0xFF4CAF50)
private val SuccessLight = Color(0xFFE8F5E9)

private val ratingLabels = listOf(
    "", "Very bad 😞", "Not great 😕", "It was okay 😐", "Good! 😊", "Loved it! 🤩"
)

private val deliveryChips = listOf("Fast delivery", "On time", "Late", "Very late")
private val foodChips =
    listOf("Delicious", "Fresh", "Good portion", "Cold food", "Wrong order", "Missing items")
private val packagingChips = listOf("Well packed", "Damaged packaging", "Eco-friendly")

data class FoodOrderSummary(
    val restaurantName: String = "Burger Palace",
    val itemName: String = "Classic Smash Burger",
    val price: String = "₹349",
    val deliveryTime: String = "28 min",
    val emoji: String = "🍔"
)

@Composable
fun YourFeedback(
    navController: NavController
) {
    var submitted by remember { mutableStateOf(false) }

    Surface(color = BgPage, modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = submitted,
            transitionSpec = {
                fadeIn(tween(350)) + slideInVertically { it / 12 } togetherWith
                        fadeOut(tween(200))
            },
            label = "feedback_anim"
        ) { done ->
            if (done) ThankYouScreen(navController)
            else FeedbackContent(onSubmit = { submitted = true })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackContent(
    onSubmit: () -> Unit,
) {
    var rating by remember { mutableIntStateOf(0) }
    var hoveredStar by remember { mutableIntStateOf(0) }
    val deliverySelected = remember { mutableStateListOf<String>() }
    val foodSelected = remember { mutableStateListOf<String>() }
    val packSelected = remember { mutableStateListOf<String>() }
    var comment by remember { mutableStateOf("") }

    val displayRating = if (hoveredStar > 0) hoveredStar else rating

    Scaffold(


        topBar = {

            TopAppBar(

                title = {
                    Text(
                        text = "Your Feedback"
                    )
                }

            )
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            FeedbackCard {
                Column {
                    SectionLabel("Overall experience")
                    Spacer(Modifier.height(12.dp))

                    // Stars
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        (1..5).forEach { i ->
                            FoodStarButton(
                                filled = i <= displayRating,
                                onClick = { rating = i; hoveredStar = 0 },
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = displayRating > 0,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Text(
                            text = ratingLabels.getOrElse(displayRating) { "" },
                            fontSize = 14.sp,
                            color = if (displayRating >= 4) OrangeMain else TextSecondary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            FeedbackCard {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🛵", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        SectionLabel("Delivery experience")
                    }
                    Spacer(Modifier.height(12.dp))
                    ChipGroup(
                        options = deliveryChips,
                        selected = deliverySelected,
                        accentBad = listOf("Late", "Very late")
                    )
                }
            }

            FeedbackCard {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🍽️", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        SectionLabel("Food quality")
                    }
                    Spacer(Modifier.height(12.dp))
                    ChipGroup(
                        options = foodChips,
                        selected = foodSelected,
                        accentBad = listOf("Cold food", "Wrong order", "Missing items")
                    )
                }
            }

            FeedbackCard {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📦", fontSize = 20.sp)
                        Spacer(Modifier.width(8.dp))
                        SectionLabel("Packaging")
                    }
                    Spacer(Modifier.height(12.dp))
                    ChipGroup(
                        options = packagingChips,
                        selected = packSelected,
                        accentBad = listOf("Damaged packaging")
                    )
                }
            }

            FeedbackCard {
                Column {
                    SectionLabel("Tell us more (optional)")
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = comment,
                        onValueChange = { comment = it },
                        placeholder = {
                            Text(
                                "Anything you'd like the restaurant or rider to know?",
                                fontSize = 13.sp,
                                color = TextHint,
                                lineHeight = 18.sp
                            )
                        },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = OrangeMain,
                            unfocusedBorderColor = DividerColor,
                            focusedContainerColor = BgInput,
                            unfocusedContainerColor = BgInput
                        ),
                        textStyle = LocalTextStyle.current.copy(
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                    )
                }
            }

            Button(
                onClick = onSubmit,
                enabled = rating > 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A1B9A),
                    contentColor = Color.White,
                    disabledContainerColor = Color(0xFF6A1B9A).copy(alpha = 0.3f),
                    disabledContentColor = Color.White.copy(alpha = 0.6f)
                )
            ) {
                Text(
                    text = "Submit feedback",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.height(8.dp))
        }
    }


}

@Composable
fun OrderSummaryCard(order: FoodOrderSummary) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = OrangeSoft),
        border = BorderStroke(1.dp, OrangeBorder.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(order.emoji, fontSize = 28.sp)
            }

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.restaurantName,
                    fontSize = 13.sp,
                    color = OrangeText,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = order.itemName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    InfoBadge("⏱ ${order.deliveryTime}")
                    InfoBadge(order.price)
                }
            }
        }
    }
}

@Composable
fun InfoBadge(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.7f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text, fontSize = 12.sp, color = OrangeText, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun FeedbackCard(content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(0.5.dp, DividerColor),
        elevation = CardDefaults.cardElevation(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            content()
        }
    }
}

@Composable
fun FoodStarButton(filled: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (filled) 1.18f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "star_scale"
    )
    val color by animateColorAsState(
        targetValue = if (filled) StarYellow else StarEmpty,
        animationSpec = tween(150),
        label = "star_color"
    )

    Text(
        text = "★",
        fontSize = 36.sp,
        color = color,
        modifier = Modifier
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    )
}

@Composable
fun ChipGroup(
    options: List<String>,
    selected: MutableList<String>,
    accentBad: List<String> = emptyList()
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { label ->
            val isSelected = label in selected
            val isBad = label in accentBad

            FoodChip(
                label = label,
                selected = isSelected,
                isBadOption = isBad,
                onClick = {
                    if (isSelected) selected.remove(label)
                    else selected.add(label)
                }
            )
        }
    }
}

@Composable
fun FoodChip(
    label: String,
    selected: Boolean,
    isBadOption: Boolean,
    onClick: () -> Unit
) {
    val selectedBg = if (isBadOption) Color(0xFFFFF0EB) else OrangeSoft
    val selectedText = if (isBadOption) Color(0xFF6A1B9A) else OrangeText
    val selectedBorder = if (isBadOption) Color(0xFFFFB99A) else OrangeBorder

    val bgColor by animateColorAsState(
        if (selected) selectedBg else Color.Transparent,
        label = "chip_bg"
    )
    val textColor by animateColorAsState(
        if (selected) selectedText else TextSecondary,
        label = "chip_text"
    )
    val borderColor by animateColorAsState(
        if (selected) selectedBorder else DividerColor,
        label = "chip_border"
    )
    val scale by animateFloatAsState(if (selected) 1.03f else 1f, label = "chip_scale")

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(999.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun ThankYouScreen(
    navController: NavController
) {
    var context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Animated success circle
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.06f,
            animationSpec = infiniteRepeatable(
                animation = tween(900, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )



        Spacer(Modifier.height(24.dp))

        Text(
            text = "Thanks for the feedback!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Your review helps our delivery partners improve.",
            fontSize = 14.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(32.dp))

        // Coupon teaser
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SuccessLight),
            border = BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🎁", fontSize = 28.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "You've earned ₹50 off",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        "Apply on your next order",
                        fontSize = 13.sp,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))


        Button(
            onClick = {

                navController.navigate(Routes.ProfileScreen)
                Toast.makeText(context, "Thank you for your feedback", Toast.LENGTH_SHORT).show()

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF6A1B9A),
                contentColor = Color.White
            )
        ) {
            Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextSecondary,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF9F7, showSystemUi = true)
@Composable
fun FoodFeedbackPreview() {
    MaterialTheme {
        YourFeedback(rememberNavController())
    }
}