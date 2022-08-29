package com.me.recipe.steps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.R

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeTheme {
                HamburgerPage()
            }
        }
    }
}

@Composable
fun HamburgerPage() {

    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Description",
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .background(Color.Yellow)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Happy Meal",
                style = TextStyle(fontSize = 26.sp)
            )
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "$5.99",
                style = TextStyle(
                    color = colorResource(R.color.purple_200),
                    fontSize = 16.sp
                ),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Text(
            text = "800 calories",
            style = TextStyle(
                fontSize = 16.sp
            ),
            modifier = Modifier.padding(16.dp)
        )
    }

}

@Composable
fun TestColumnAndRow() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(BorderStroke(1.dp, Color.Black)),
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = "Hello everyone",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Hello everyone",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.padding(top = 10.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(BorderStroke(1.dp, Color.Black)),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "Hello everyone",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                text = "Hello everyone",
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Button(
            modifier = Modifier
                .height(60.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { }) {
            Text(text = "hi everyone")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecipeTheme {
        HamburgerPage()
    }
}