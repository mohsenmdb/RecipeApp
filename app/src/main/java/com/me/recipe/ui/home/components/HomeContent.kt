@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.HomeContract
import com.me.recipe.ui.navigation.HomeDestination
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
    if (state.sliderRecipes.isEmpty()) return
    if (state.categoriesRecipes.isEmpty()) return
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
        items(state.categoriesRecipes) { category ->
            RecipeCategoryItem(
                category = category,
                event = event,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@Composable
internal fun RecipeCategoryItem(
    category: CategoryRecipe,
    event: (HomeContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = category.category.value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 16.dp),
        )
        LazyRow {
            items(category.recipes) {
                RecipeCard(
                    recipe = it,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onClick = {
                        event.invoke(HomeContract.Event.ClickOnRecipeEvent(it))
                    },
                    onLongClick = {
                        event.invoke(HomeContract.Event.LongClickOnRecipeEvent(it.title))
                    },
                    recipePageStarter = HomeDestination.route,
                )
            }
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
