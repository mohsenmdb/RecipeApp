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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.GenericDialog
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.recipelist.RecipeListContract
import com.me.recipe.ui.recipelist.component.shimmer.RecipeListShimmer
import com.me.recipe.ui.recipelist.showLoadingProgressBar
import com.me.recipe.ui.recipelist.showShimmer
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.extention.encodeToUtf8
import kotlinx.coroutines.launch

@Composable
internal fun RecipeListContent(
    padding: PaddingValues,
    state: RecipeListContract.State,
    event: (RecipeListContract.Event) -> Unit,
    navigateToRecipePage: (id: Int, title: String, image: String) -> Unit,
    snackbarHostState: SnackbarHostState,
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
                navigateToRecipePage = navigateToRecipePage,
                snackbarHostState = snackbarHostState,
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
    navigateToRecipePage: (id: Int, title: String, image: String) -> Unit,
    snackbarHostState: SnackbarHostState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val errorMessage = stringResource(id = R.string.something_went_wrong)
    val actionOk = stringResource(id = R.string.ok)
    LazyColumn(
        modifier = modifier.testTag("testTag_RecipeList")
    ) {
        itemsIndexed(state.recipes) { index, recipe ->
            event.invoke(RecipeListContract.Event.OnChangeRecipeScrollPosition(index))
            event.invoke(RecipeListContract.Event.NextPageEvent(index))

            RecipeCard(
                recipe = recipe,
                onClick = {
                    if (recipe.id != Recipe.EMPTY.id) {
                        navigateToRecipePage(
                            recipe.id,
                            recipe.title,
                            recipe.featuredImage.encodeToUtf8(),
                        )
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(errorMessage, actionOk)
                        }
                    }
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
                navigateToRecipePage = { _, _, _ -> },
                event = {},
                snackbarHostState = SnackbarHostState(),
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
