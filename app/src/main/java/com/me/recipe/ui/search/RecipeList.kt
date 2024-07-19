@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.search.component.RecipeCard
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.theme.RecipeTheme


@Composable
internal fun RecipeList(
    state: SearchContract.State,
    event: (SearchContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.testTag("testTag_recipeList"),
    ) {
        itemsIndexed(state.recipes) { index, recipe ->
            event.invoke(SearchContract.Event.OnChangeRecipeScrollPosition(index))

            RecipeCard(
                recipe = recipe,
                onClick = {
                    event.invoke(SearchContract.Event.ClickOnRecipeEvent(recipe))
                },
                onLongClick = {
                    event.invoke(SearchContract.Event.LongClickOnRecipeEvent(recipe.title))
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            RecipeList(
                state = SearchContract.State.testData(),
                event = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
