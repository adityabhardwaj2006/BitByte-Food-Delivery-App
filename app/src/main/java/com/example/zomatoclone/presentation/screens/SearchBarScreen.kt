package com.example.zomatoclone.presentation.screens

import android.R.attr.onClick
import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.zomatoclone.R
import com.example.zomatoclone.data.models.FoodCategory
import com.example.zomatoclone.presentation.ViewModels.Food
import com.example.zomatoclone.presentation.ViewModels.SearchViewModel
import com.example.zomatoclone.presentation.ViewModels.SharedViewModel
import com.example.zomatoclone.presentation.ViewModels.ShortCutViewModel
import com.example.zomatoclone.presentation.navigation.Routes
import java.util.Locale

@Composable
fun SearchBarScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    searchViewModel: SearchViewModel,
    shortCutViewModel: ShortCutViewModel
) {
    val query by searchViewModel.searchText.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            val spokenText = matches?.firstOrNull() ?: ""
            searchViewModel.onTextSearch(spokenText)

        }
    }
    Log.d("VM_CHECK", shortCutViewModel.hashCode().toString())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .background(Color.White)
    ) {
        var focusRequester = remember { FocusRequester() }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp), elevation = 5.dp, shape = RoundedCornerShape(16.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp)
                        .systemBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.arrowback),
                        contentDescription = "back",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { navController.popBackStack() },
                        tint = Color(0xFF4CAF50),

                        )
                    Spacer(modifier = Modifier.width(8.dp))

                    BasicTextField(
                        value = query,
                        onValueChange = {
                            searchViewModel.onTextSearch(it)
                        },
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = Color.Gray
                        ),
                        decorationBox = { innerTextField ->
                            Box {
                                if (query.isEmpty()) {
                                    Text(
                                        text = "Search...",
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                                innerTextField()
                            }

                        },
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .focusable()
                            .clickable {
                                focusRequester.requestFocus()
                            },
                        singleLine = true

                    )
                    Spacer(modifier = Modifier.weight(1f))
                    VerticalDivider(modifier = Modifier.height(22.dp), thickness = 1.dp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        painterResource(R.drawable.mic),
                        contentDescription = "Mic",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier
                            .clickable {

                                val intent =
                                    Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                        )
                                        putExtra(
                                            RecognizerIntent.EXTRA_LANGUAGE,
                                            Locale.getDefault()
                                        )
                                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
                                    }

                                launcher.launch(intent)
                            })

                }

            }
        }
        TabItem(searchViewModel, shortCutViewModel, navController)

    }

}

@Composable
fun TabItem(
    searchViewModel: SearchViewModel, shortCutViewModel: ShortCutViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .padding(start = 15.dp)
        ) {
            Text(
                text = "WHAT'S ON YOUR MIND?", fontSize = 14.sp,
                color = Color.Gray, fontFamily = FontFamily.SansSerif, letterSpacing = 2.sp
            )
            val foodCategories = searchViewModel.foodList.collectAsState()

            LazyVerticalGrid(
                columns = GridCells.Fixed(3)   //Display 3 item per row
                , Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)

            ) {
                items(foodCategories.value.size) { index ->

                    val category = foodCategories.value[index]
                    FoodCategoryItem(
                        category = category,
                        onClick = {

                        },
                        shortCutViewModel,
                        navController
                    )

                }

            }
        }


    }
}

@Composable
fun FoodCategoryItem(
    category: Food,
    onClick: () -> Unit,
    shortCutViewModel: ShortCutViewModel,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(100.dp)
            .clickable {
                shortCutViewModel.updateTabName(category.name)
                navController.navigate(Routes.DeliveryScreen)
            }
    ) {

        Image(
            painter = painterResource(id = category.imageRes),
            contentDescription = category.name,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = category.name,
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

