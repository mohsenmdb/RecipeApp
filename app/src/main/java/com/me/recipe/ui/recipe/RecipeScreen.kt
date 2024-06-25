@file:OptIn(
    InternalCoroutinesApi::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalSharedTransitionApi::class,
)

package com.me.recipe.ui.recipe

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.ui.component.util.DefaultSnackbar
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.recipe.components.RecipeDetail
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
internal fun RecipeScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: RecipeViewModel = hiltViewModel(),
) {
    val (state, effect, event) = use(viewModel = viewModel)
    RecipeScreen(effect, state, event, sharedTransitionScope, animatedVisibilityScope)
}

@Composable
internal fun RecipeScreen(
    effect: Flow<RecipeContract.Effect>,
    state: RecipeContract.State,
    event: (RecipeContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    effect.collectInLaunchedEffect { effect ->
        when (effect) {
            is RecipeContract.Effect.ShowSnackbar -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(effect.message, "Ok")
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
    ) { padding ->
        RecipeDetail(
            recipe = state.recipe,
            isLoading = state.loading,
            startDestination = state.startDestination,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = Modifier.padding(padding),
        )
    }
}

@Preview
@Composable
private fun RecipeScreenPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            RecipeScreen(
                event = {},
                effect = flowOf(),
                state = RecipeContract.State.testData(),
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
