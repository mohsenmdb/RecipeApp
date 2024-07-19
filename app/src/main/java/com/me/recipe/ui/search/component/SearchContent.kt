@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.search.component

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
import com.me.recipe.ui.component.util.GenericDialog
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.search.SearchContract
import com.me.recipe.ui.search.component.shimmer.SearchShimmer
import com.me.recipe.ui.search.showLoadingProgressBar
import com.me.recipe.ui.search.showShimmer
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun SearchContent(
    padding: PaddingValues,
    state: SearchContract.State,
    event: (SearchContract.Event) -> Unit,
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
            SearchShimmer(250.dp)
        } else {
            RecipeList(
                recipes = state.recipes,
                event = event,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )

            LoadingView(isVisible = state.showLoadingProgressBar)

            state.errors?.let { GenericDialog(it) }
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            SearchContent(
                padding = PaddingValues(16.dp),
                state = SearchContract.State.testData(),
                event = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
