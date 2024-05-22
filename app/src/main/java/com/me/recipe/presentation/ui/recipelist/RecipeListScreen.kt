@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.presentation.ui.recipelist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.R
import com.me.recipe.presentation.component.util.DefaultSnackbar
import com.me.recipe.presentation.component.util.SharedTransitionLayoutPreview
import com.me.recipe.presentation.ui.recipelist.component.RecipeListContent
import com.me.recipe.presentation.ui.recipelist.component.SearchAppBar
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun RecipeListScreen(
    navigateToRecipePage: (id: Int, title: String, image: String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: RecipeListViewModel = hiltViewModel(),
) {
    val (state, effect, event) = use(viewModel = viewModel)
    RecipeListScreen(
        effect = effect,
        state = state,
        event = event,
        modifier = modifier,
        navigateToRecipePage = navigateToRecipePage,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@Composable
@OptIn(InternalCoroutinesApi::class)
private fun RecipeListScreen(
    effect: Flow<RecipeListContract.Effect>,
    state: RecipeListContract.State,
    event: (RecipeListContract.Event) -> Unit,
    navigateToRecipePage: (id: Int, title: String, image: String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val actionOk = stringResource(id = R.string.ok)

    effect.collectInLaunchedEffect { effect ->
        when (effect) {
            is RecipeListContract.Effect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(effect.message, actionOk)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        topBar = {
            SearchAppBar(
                query = state.query,
                selectedCategory = state.selectedCategory,
                categoryScrollPosition = state.categoryScrollPosition,
                onQueryChanged = { event.invoke(RecipeListContract.Event.OnQueryChanged(it)) },
                newSearch = { event.invoke(RecipeListContract.Event.NewSearchEvent) },
                onSelectedCategoryChanged = {
                    event.invoke(RecipeListContract.Event.OnSelectedCategoryChanged(it))
                },
                onCategoryScrollPositionChanged = { position, offset ->
                    event.invoke(
                        RecipeListContract.Event.OnCategoryScrollPositionChanged(position, offset),
                    )
                },
                onToggleTheme = { event.invoke(RecipeListContract.Event.ToggleDarkTheme) },
            )
        },
        modifier = modifier,
    ) { padding ->

        RecipeListContent(
            padding = padding,
            state = state,
            event = event,
            navigateToRecipePage = navigateToRecipePage,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            snackbarHostState = snackbarHostState,
        )
    }
}

@Preview
@Composable
private fun RecipeListScreenPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            RecipeListScreen(
                event = {},
                effect = flowOf(),
                state = RecipeListContract.State.testData(),
                navigateToRecipePage = { _, _, _ -> },
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
