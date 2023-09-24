package com.me.recipe.presentation.ui.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.me.recipe.presentation.component.DefaultSnackbar
import com.me.recipe.presentation.component.LoadingRecipeShimmer
import com.me.recipe.presentation.component.RecipeView
import com.me.recipe.presentation.ui.navigation.NavigationDestination

object RecipeDestination : NavigationDestination {
    override val route = "Recipe"
    override val titleRes = R.string.navigate_recipe_title
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun RecipeScreen() {
    RecipeScreen(hiltViewModel())
}

@Composable
private fun RecipeScreen(
    viewModel: RecipeViewModel,
) {

    val recipe = viewModel.recipe.value
    val isLoading = viewModel.isLoading.value
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = {
            DefaultSnackbar(snackbarHostState = snackbarHostState) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colors.background)
        ) {
            if (isLoading && recipe == null)
                LoadingRecipeShimmer(imageHeight = 250.dp)
            else
                recipe?.let {
                    RecipeView(recipe = recipe)
                }
        }
    }
}