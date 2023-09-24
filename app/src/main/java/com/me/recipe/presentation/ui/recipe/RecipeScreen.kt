package com.me.recipe.presentation.ui.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.me.recipe.presentation.ui.navigation.NavigationDestination
import com.me.recipe.R
import com.me.recipe.presentation.component.DefaultSnackbar
import com.me.recipe.presentation.component.LoadingRecipeShimmer
import com.me.recipe.presentation.component.RecipeView

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
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }
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

            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
            }
        }
    }
}