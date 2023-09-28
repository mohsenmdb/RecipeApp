package com.me.recipe.presentation.ui.recipe_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.R
import com.me.recipe.presentation.component.CircularIndeterminateProgressBar
import com.me.recipe.presentation.component.DefaultSnackbar
import com.me.recipe.presentation.component.GenericDialog
import com.me.recipe.presentation.component.LoadingRecipeListShimmer
import com.me.recipe.presentation.component.RecipeCard
import com.me.recipe.presentation.component.SearchAppBar
import com.me.recipe.presentation.ui.navigation.NavigationDestination
import com.me.recipe.presentation.ui.recipe_list.RecipeListViewModel.Companion.PAGE_SIZE
import kotlinx.coroutines.launch

object RecipeListDestination : NavigationDestination {
    override val route = "RecipeList"
    override val titleRes = R.string.navigate_recipe_list_title
}

@Composable
fun RecipeListScreen(
    navigateToRecipePage: (recipeId: Int) -> Unit
) {
    RecipeListScreen(
        viewModel = hiltViewModel(),
        navigateToRecipePage
    )
}


@Composable
private fun RecipeListScreen(
    viewModel: RecipeListViewModel,
    navigateToRecipePage: (recipeId: Int) -> Unit
) {

    val recipes = viewModel.recipes.value
    val errors = viewModel.errors.value
    val query = viewModel.query.value
    val selectedCategory = viewModel.selectedCategory.value
    val isLoading = viewModel.loading.value
    val page = viewModel.page.value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
        topBar = {
            SearchAppBar(
                query = query,
                selectedCategory = selectedCategory,
                categoryScrollPosition = viewModel.categoryScrollPosition,
                onQueryChanged = viewModel::onQueryChanged,
                newSearch = { viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent) },
                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                onCategoryScrollPositionChanged = viewModel::onCategoryScrollPositionChanged,
                onToggleTheme = {
                    viewModel.toggleDarkTheme()
                }
            )
        },
//            bottomBar = { MyBottomNav() },
//            drawerContent = { MyDrawer() },
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colors.background)
        ) {
            if (isLoading && recipes.isEmpty())
                LoadingRecipeListShimmer(250.dp)
            else
                LazyColumn {
                    itemsIndexed(recipes) { index, recipe ->
                        viewModel.onChangeRecipeScrollPosition(index)
                        if ((index + 1) >= (page * PAGE_SIZE) && !isLoading)
                            viewModel.onTriggerEvent(RecipeListEvent.NextPageEvent)

                        RecipeCard(
                            recipe = recipe,
                            onClick = {
                                recipe.id?.let {
                                    navigateToRecipePage(it)
                                } ?: coroutineScope.launch {
                                    snackbarHostState.showSnackbar("there is no id", "Ok")
                                }
                            }
                        )
                    }
                }

            CircularIndeterminateProgressBar(isVisible = (isLoading && recipes.isNotEmpty()))

            if (errors != null) {
                GenericDialog(
                    onDismiss = errors.onDismiss,
                    title = errors.title,
                    description = errors.description,
                    positiveAction = errors.positiveAction,
                    negativeAction = errors.negativeAction
                )
            }
        }
    }
}