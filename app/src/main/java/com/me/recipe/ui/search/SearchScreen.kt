@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.search

import androidx.activity.compose.BackHandler
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
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.NavigateToHomePage
import com.me.recipe.ui.component.util.NavigateToRecipePage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.search.component.SearchContent
import com.me.recipe.ui.search.component.SearchAppBar
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun SearchScreen(
    navigateToRecipePage: NavigateToRecipePage,
    navigateToHomePage: NavigateToHomePage,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val (state, effect, event) = use(viewModel = viewModel)
    SearchScreen(
        effect = effect,
        state = state,
        event = event,
        modifier = modifier,
        navigateToHomePage = navigateToHomePage,
        navigateToRecipePage = navigateToRecipePage,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )
}

@Composable
@OptIn(InternalCoroutinesApi::class)
internal fun SearchScreen(
    effect: Flow<SearchContract.Effect>,
    state: SearchContract.State,
    event: (SearchContract.Event) -> Unit,
    navigateToHomePage: NavigateToHomePage,
    navigateToRecipePage: NavigateToRecipePage,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val actionOk = stringResource(id = R.string.ok)

    BackHandler {
        navigateToHomePage.invoke()
    }

    effect.collectInLaunchedEffect { effect ->
        when (effect) {
            is SearchContract.Effect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(effect.message, actionOk)
                }
            }
            is SearchContract.Effect.NavigateToRecipePage -> {
                navigateToRecipePage(effect.recipe)
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
                onQueryChanged = { event.invoke(SearchContract.Event.OnQueryChanged(it)) },
                newSearch = { event.invoke(SearchContract.Event.NewSearchEvent) },
                onSearchClearClicked = { event.invoke(SearchContract.Event.SearchClearEvent) },
                onSelectedCategoryChanged = {
                    event.invoke(SearchContract.Event.OnSelectedCategoryChanged(it))
                },
                onCategoryScrollPositionChanged = { position, offset ->
                    event.invoke(
                        SearchContract.Event.OnCategoryScrollPositionChanged(position, offset),
                    )
                },
            )
        },
        modifier = modifier,
    ) { padding ->

        SearchContent(
            padding = padding,
            state = state,
            event = event,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@Preview
@Composable
private fun SearchScreenPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            SearchScreen(
                event = {},
                effect = flowOf(),
                state = SearchContract.State.testData(),
                navigateToRecipePage = { _ -> },
                navigateToHomePage = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
