package com.example.zomatoclone.presentation.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zomatoclone.R

@Composable
fun CheckboxComponent(value: String) {
    val context = LocalContext.current
    val checkState = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 45.dp)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = checkState.value,
            onCheckedChange = {
                checkState.value = !checkState.value
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = Color.Gray
            )
        )

        Text(
            text = value,
            modifier = Modifier.padding(top = 11.dp),
            fontSize = 11.sp
        )

        Text(
            text = "Term",
            modifier = Modifier
                .padding(start = 2.dp, top = 11.dp)
                .clickable {
                    Toast.makeText(
                        context,
                        "Term and Conditions",
                        Toast.LENGTH_LONG
                    ).show()
                },
            fontSize = 11.sp,
            color = colorResource(R.color.purple_200)
        )

        Text(
            text = "and",
            modifier = Modifier.padding(start = 2.dp, top = 11.dp, end = 2.dp),
            fontSize = 11.sp
        )

        Text(
            text = "Conditions",
            modifier = Modifier
                .padding(top = 11.dp)
                .clickable {
                    Toast.makeText(
                        context,
                        "Term and Conditions",
                        Toast.LENGTH_LONG
                    ).show()
                },
            fontSize = 11.sp,
            color = colorResource(R.color.purple_500)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun showU(){
    CheckboxComponent("")
}