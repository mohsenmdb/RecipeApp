package com.me.recipe.presentation.ui.recipelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.presentation.component.CircularIndeterminateProgressBar
import com.me.recipe.presentation.component.LoadingRecipeListShimmer
import com.me.recipe.presentation.component.RecipeCard
import com.me.recipe.presentation.component.SearchAppBar
import com.me.recipe.presentation.component.util.DefaultSnackbar
import com.me.recipe.presentation.component.util.GenericDialog
import com.me.recipe.presentation.ui.recipelist.RecipeListViewModel.Companion.PAGE_SIZE
import com.me.recipe.util.compose.collectInLaunchedEffect
import com.me.recipe.util.compose.use
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch

@OptIn(InternalCoroutinesApi::class)
@Composable
fun RecipeListScreen(
    viewModel: RecipeListViewModel = hiltViewModel(),
    navigateToRecipePage: (recipeId: Int) -> Unit,
) {
    val (state, effect, event) = use(viewModel = viewModel)
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    effect.collectInLaunchedEffect { effect ->
        when (effect) {
            is RecipeListContract.Effect.ShowSnackbar -> {
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
        topBar = {
            SearchAppBar(
                query = state.query,
                selectedCategory = state.selectedCategory,
                categoryScrollPosition = state.categoryScrollPosition,
                onQueryChanged = viewModel::onQueryChanged,
                newSearch = { event.invoke(RecipeListContract.Event.NewSearchEvent) },
                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                onCategoryScrollPositionChanged = viewModel::onCategoryScrollPositionChanged,
                onToggleTheme = {
                    viewModel.toggleDarkTheme()
                },
            )
        },
//            bottomBar = { MyBottomNav() },
//            drawerContent = { MyDrawer() },
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
        ) {
            if (state.loading && state.recipes.isEmpty()) {
                LoadingRecipeListShimmer(250.dp)
            } else {
                LazyColumn {
                    itemsIndexed(state.recipes) { index, recipe ->
                        viewModel.onChangeRecipeScrollPosition(index)
                        if ((index + 1) >= (state.page * PAGE_SIZE) && !state.loading) {
                            event.invoke(RecipeListContract.Event.NextPageEvent)
                        }

                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                if (recipe.id != Recipe.EMPTY.id) {
                                    navigateToRecipePage(recipe.id)
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("there is no id", "Ok")
                                    }
                                }
                            },
                            onLongClick = {
                                event.invoke(RecipeListContract.Event.LongClickOnRecipeEvent(recipe.title))
                            },
                        )
                    }
                }

                CircularIndeterminateProgressBar(isVisible = (state.loading && state.recipes.isNotEmpty()))

                if (state.errors != null) {
                    GenericDialog(
                        onDismiss = state.errors.onDismiss,
                        title = state.errors.title,
                        description = state.errors.description,
                        positiveAction = state.errors.positiveAction,
                        negativeAction = state.errors.negativeAction,
                    )
                }
            }
        }
    }
}
