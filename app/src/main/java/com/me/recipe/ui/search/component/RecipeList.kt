@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.search.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.search.SearchContract
import com.me.recipe.ui.theme.RecipeTheme
import kotlinx.collections.immutable.ImmutableList


@Composable
internal fun RecipeList(
    recipes: ImmutableList<Recipe>,
    event: (SearchContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.testTag("testTag_recipeList"),
    ) {
        itemsIndexed(recipes) { index, recipe ->
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
                recipes = SearchContract.State.testData().recipes,
                event = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
