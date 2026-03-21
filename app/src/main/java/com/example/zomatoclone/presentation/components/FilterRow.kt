package com.example.zomatoclone.presentation.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zomatoclone.R

@Composable
fun FilterRow(filters: List<String>) {

    var selectedFilter by remember { mutableStateOf<String?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LazyRow for horizontally scrolling list of chips
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = {
                        // Toggle selection: if same filter clicked, clear selection; otherwise select it
                        selectedFilter = if (selectedFilter == filter) null else filter
                    },
                    label = { Text(filter, fontSize = 12.sp) },
                    leadingIcon = {
                        // Show an icon only for particular filters; keep original asset tint by using Color.Unspecified
                        when (filter) {
                            "Filter" -> Icon(
                                painter = painterResource(id = R.drawable.dining),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Unspecified
                            )
                            "Flash Sale" -> Icon(
                                painter = painterResource(id = R.drawable.snack_meal),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Unspecified
                            )
                            "Under 30 minutes" -> Icon(
                                painter = painterResource(id = R.drawable.timer),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Unspecified
                            )
                            "Rating" -> Icon(
                                painter = painterResource(id = R.drawable.rating),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Unspecified
                            )
                            "Schedule" -> Icon(
                                painter = painterResource(id = R.drawable.notes),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.Unspecified
                            )
                            else -> {}
                        }
                    },
                    trailingIcon = {
                        // For the "Filter" chip show a dropdown arrow; otherwise show a close icon when that chip is selected
                        if (filter == "Filter") {
                            Icon(
                                painter = painterResource(id = R.drawable.arrowdown),
                                contentDescription = null,
                                modifier = Modifier.size(22.dp),
                                tint = Color.Unspecified
                            )
                        } else if (selectedFilter == filter) {
                            Icon(
                                painter = painterResource(id = R.drawable.close),
                                contentDescription = "Clear",
                                modifier = Modifier.size(22.dp),
                                tint = Color.Unspecified
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.White,
                        labelColor = Color.DarkGray
                    ),
                    enabled = true,

                )
            }
        }
    }
}


