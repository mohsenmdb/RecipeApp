@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.HomeContract
import com.me.recipe.ui.recipelist.component.RecipeCard
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun HomeContent(
    padding: PaddingValues,
    state: HomeContract.State,
    event: (HomeContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    if (state.recipes.isEmpty()) return
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background),
    ) {
        RecipeCard(
            recipe = state.recipes[0],
            onClick = {
                event.invoke(HomeContract.Event.ClickOnRecipeEvent(state.recipes[0]))
            },
            onLongClick = { },
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
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
