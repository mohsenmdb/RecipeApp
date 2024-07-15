@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.HomeContract
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun HomeContent(
    padding: PaddingValues,
    state: HomeContract.State,
    event: (HomeContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background),
    ) {
        item {
            HomeSlider(
                recipes = state.sliderRecipes,
                event = event,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
        itemsIndexed(state.categoriesRecipes) { index, category ->
            if (index == 0) {
                RecipeCategoryHorizontalItem(
                    category = category,
                    event = event,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                return@itemsIndexed
            }
            RecipeCategoryVerticalItem(
                category = category,
                event = event,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@Preview
@Composable
private fun RecipeListContentPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            HomeContent(
                padding = PaddingValues(16.dp),
                state = HomeContract.State.testData(),
                event = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
