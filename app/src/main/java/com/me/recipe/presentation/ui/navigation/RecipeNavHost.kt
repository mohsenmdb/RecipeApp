package com.me.recipe.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.me.recipe.presentation.ui.coming_soon.ComingSoonScreen
import com.me.recipe.presentation.ui.recipe.RecipeScreen
import com.me.recipe.presentation.ui.recipe_list.RecipeListScreen
import com.me.recipe.presentation.ui.splash.SplashScreen

@Composable
fun RecipeNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = SplashDestination.route,
        modifier = modifier
    ) {
        composable(route = SplashDestination.route) {
            SplashScreen(
                navigateToRecipeList = {
                    navController.navigateSingleTopFromSplash(RecipeListDestination.route)
                }
            )
        }
        composable(route = RecipeListDestination.route) {
            RecipeListScreen(
                navigateToRecipePage = {
                    navController.navigateSingleTopTo("${RecipeDestination.route}/${it}")
                }
            )
        }
        composable(
            route = RecipeDestination.routeWithArgs,
            arguments = RecipeDestination.arguments,
            deepLinks = RecipeDestination.deepLinks
        ) {
            RecipeScreen()
        }
        composable(route = ComingSoonDestination.route) {
            ComingSoonScreen()
        }
    }
}
