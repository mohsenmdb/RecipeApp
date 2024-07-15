@file:OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun SliderCard(
    recipe: Recipe,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            with(sharedTransitionScope) {
                CoilImage(
                    data = recipe.featuredImage,
                    contentDescription = "recipe image",
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "image-${recipe.uid}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                VerticalGradientBox()
                RecipeTitle(
                    recipe,
                    animatedVisibilityScope,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }
}

@Composable
private fun SharedTransitionScope.RecipeTitle(
    recipe: Recipe,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Text(
        text = recipe.title,
        modifier = modifier
            .sharedBounds(
                rememberSharedContentState(key = "title-${recipe.uid}"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 12.dp),
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
    )
}

@Composable
private fun BoxWithConstraintsScope.VerticalGradientBox() {
    Box(
        modifier = Modifier
            .height(maxHeight / 2)
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0x00000000),
                        Color(0x00000000),
                        Color(0x66000000),
                        Color(0x99000000),
                        Color(0xBF000000),
                    ),
                ),
            ),
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFF292C3C,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun SliderCardPreview() {
    RecipeTheme {
        SharedTransitionLayoutPreview {
            SliderCard(
                recipe = Recipe.testData(),
                onClick = {},
                onLongClick = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
