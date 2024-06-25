@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home

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
import com.me.recipe.ui.component.util.NavigateToRecipePage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.home.components.HomeContent
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreen(
    navigateToRecipePage: NavigateToRecipePage,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val (state, effect, event) = use(viewModel = viewModel)
    HomeScreen(
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
internal fun HomeScreen(
    effect: Flow<HomeContract.Effect>,
    state: HomeContract.State,
    event: (HomeContract.Event) -> Unit,
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
            is HomeContract.Effect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(effect.message, actionOk)
                }
            }
            is HomeContract.Effect.NavigateToRecipePage -> {
                navigateToRecipePage(effect.recipe.id, effect.recipe.title, effect.recipe.featuredImage)
            }
        }
    }

    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        modifier = modifier,
    ) { padding ->

        HomeContent(
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
private fun RecipeListScreenPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            HomeScreen(
                event = {},
                effect = flowOf(),
                state = HomeContract.State.testData(),
                navigateToRecipePage = { _, _, _ -> },
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
