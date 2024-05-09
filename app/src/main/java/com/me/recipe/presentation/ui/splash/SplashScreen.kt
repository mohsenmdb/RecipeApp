package com.me.recipe.presentation.ui.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.ui.theme.RecipeTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToRecipeList: () -> Unit,
) {
    val animationState = remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Rocket(
            isRocketEnabled = animationState.value,
            maxWidth = maxWidth,
            maxHeight = maxHeight,
        )
    }
    LaunchedEffect(key1 = animationState) {
        delay(500)
        animationState.value = true
        delay(2000)
        navigateToRecipeList()
    }
}

@Composable
fun Rocket(
    isRocketEnabled: Boolean,
    maxWidth: Dp,
    maxHeight: Dp,
) {
    val resource: Painter
    val modifier: Modifier
    val rocketSize = 200.dp
    val positionState: Float by animateFloatAsState(
        targetValue = if (!isRocketEnabled) 0f else 1f,
        animationSpec = tween(
            durationMillis = 2000,
            easing = LinearEasing,
        ),
        label = "",
    )
    if (!isRocketEnabled) {
        resource = painterResource(id = R.drawable.rocket_intial)
        modifier = Modifier.offset(
            y = maxHeight - rocketSize,
        )
    } else {
        val infiniteTransition = rememberInfiniteTransition(label = "")
        val engineState = infiniteTransition.animateFloat(
            label = "",
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 500,
                    easing = LinearEasing,
                ),
            ),
        )
        resource = if (engineState.value <= .5f) {
            painterResource(id = R.drawable.rocket1)
        } else {
            painterResource(id = R.drawable.rocket2)
        }
        modifier = Modifier.offset(
            x = (maxWidth - rocketSize) * positionState,
            y = (maxHeight - rocketSize) - (maxHeight) * positionState,
        )
    }
    Image(
        modifier = modifier.size(rocketSize),
        painter = resource,
        contentDescription = "A Rocket",
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecipeTheme {
        SplashScreen(navigateToRecipeList = {})
    }
}
