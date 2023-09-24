package com.me.recipe.presentation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.me.recipe.presentation.ui.navigation.RecipeNavHost

/**
 * Top level composable that represents screens for the application.
 */
@Composable
fun RecipeApp(navController: NavHostController = rememberNavController()) {
    RecipeNavHost(navController = navController)
}
