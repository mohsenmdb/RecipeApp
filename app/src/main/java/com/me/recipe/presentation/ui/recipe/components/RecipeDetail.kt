@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.presentation.ui.recipe.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.presentation.component.util.SharedTransitionLayoutTest
import com.me.recipe.presentation.ui.recipe.components.image.RecipeImage
import com.me.recipe.ui.theme.RecipeTheme

@Composable
fun RecipeDetail(
    recipe: Recipe,
    isLoading: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        with(sharedTransitionScope) {
            RecipeImage(
                id = recipe.id,
                image = recipe.featuredImage,
                animatedVisibilityScope = animatedVisibilityScope,
            )
            RecipeContent(
                recipe = recipe,
                animatedVisibilityScope = animatedVisibilityScope,
                isLoading = isLoading,
            )
        }
    }
}

@Preview
@Composable
private fun RecipeDetailPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutTest {
            RecipeDetail(
                recipe = Recipe.testData(),
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
                isLoading = false,
            )
        }
    }
}
