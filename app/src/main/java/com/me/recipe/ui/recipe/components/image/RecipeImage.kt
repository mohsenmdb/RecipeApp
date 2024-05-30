@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.recipe.components.image

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun SharedTransitionScope.RecipeImage(
    id: Int,
    image: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    CoilImage(
        data = image,
        contentDescription = "Recipe Featured Image",
        modifier = modifier
            .sharedBounds(
                rememberSharedContentState(key = "image-$id"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .fillMaxWidth()
            .height(250.dp),
        contentScale = ContentScale.Crop,
    )
}

@Preview
@Composable
private fun RecipeImagePreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            RecipeImage(
                id = Recipe.testData().id,
                image = Recipe.testData().featuredImage,
                animatedVisibilityScope = it,
            )
        }
    }
}
