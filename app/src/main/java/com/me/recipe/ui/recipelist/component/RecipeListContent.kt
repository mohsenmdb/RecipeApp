@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.recipelist.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.component.util.GenericDialog
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.recipelist.RecipeListContract
import com.me.recipe.ui.recipelist.component.shimmer.RecipeListShimmer
import com.me.recipe.ui.recipelist.showLoadingProgressBar
import com.me.recipe.ui.recipelist.showShimmer
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RecipeListContent(
    padding: PaddingValues,
    state: RecipeListContract.State,
    event: (RecipeListContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (state.showShimmer) {
            RecipeListShimmer(250.dp)
        } else {
            RecipeList(
                state = state,
                event = event,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )

            LoadingView(isVisible = state.showLoadingProgressBar)

            state.errors?.let { GenericDialog(it) }
        }
    }
}

@Composable
private fun RecipeList(
    state: RecipeListContract.State,
    event: (RecipeListContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.testTag("testTag_RecipeList"),
    ) {
        itemsIndexed(state.recipes) { index, recipe ->
            event.invoke(RecipeListContract.Event.OnChangeRecipeScrollPosition(index))

            RecipeCard(
                recipe = recipe,
                onClick = {
                    event.invoke(RecipeListContract.Event.ClickOnRecipeEvent(recipe))
                },
                onLongClick = {
                    event.invoke(RecipeListContract.Event.LongClickOnRecipeEvent(recipe.title))
                },
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
            RecipeListContent(
                padding = PaddingValues(16.dp),
                state = RecipeListContract.State.testData(),
                event = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
