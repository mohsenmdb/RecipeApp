package com.me.recipe.ui.home.components.shimmer

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun HomeShimmer(
    modifier: Modifier = Modifier,
    imageHeight: Dp = 350.dp,
    padding: Dp = 16.dp,
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .testTag("testTag_HomeShimmer"),
    ) {
        val cardWidthPx = with(LocalDensity.current) { (maxWidth - (padding * 2)).toPx() }
        val cardHeightPx = with(LocalDensity.current) { (imageHeight - padding).toPx() }
        val gradientWidth: Float = (0.2f * cardHeightPx)

        val infiniteTransition = rememberInfiniteTransition(label = "")
        val xCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardWidthPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "",
        )
        val yCardShimmer = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = (cardHeightPx + gradientWidth),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    easing = LinearEasing,
                    delayMillis = 300,
                ),
                repeatMode = RepeatMode.Restart,
            ),
            label = "",
        )

        val colors = listOf(
            Color.LightGray.copy(alpha = .9f),
            Color.LightGray.copy(alpha = .3f),
            Color.LightGray.copy(alpha = .9f),
        )
        val brush = Brush.linearGradient(
            colors,
            start = Offset(xCardShimmer.value - gradientWidth, yCardShimmer.value - gradientWidth),
            end = Offset(xCardShimmer.value, yCardShimmer.value),
        )
        HomeShimmerContent(padding, imageHeight, brush)
    }
}

@Composable
private fun HomeShimmerContent(
    padding: Dp,
    imageHeight: Dp,
    brush: Brush
) {
    Column(modifier = Modifier.padding(padding)) {
        HomeSliderShimmer(imageHeight, brush)
        RecipeCategoriesShimmer(brush)
    }
}

@Composable
private fun RecipeCategoriesShimmer(
    brush: Brush
) {
    (1..3).forEach { index ->
        Spacer(modifier = Modifier.height(16.dp))
        RecipeRowShimmer(
            isHorizontalItemType = index == 1,
            brush = brush
        )
    }
}

@Composable
private fun RecipeRowShimmer(
    isHorizontalItemType: Boolean,
    brush: Brush,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)){
        (1..3).forEach { _ ->
            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier
                    .padding(vertical = 8.dp),
            ) {
                Spacer(
                    modifier = Modifier
                        .width(if (isHorizontalItemType) 250.dp else 200.dp)
                        .height(if (isHorizontalItemType)150.dp else 200.dp)
                        .background(brush = brush),
                )
            }
        }
    }
}

@Composable
private fun HomeSliderShimmer(
    imageHeight: Dp,
    brush: Brush
) {
    Surface(
        shape = MaterialTheme.shapes.small,
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
                .background(brush = brush),
        )
    }
}

@Preview
@Composable
private fun HomeShimmerPreview() {
    HomeShimmer()
}
