package com.me.recipe.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.me.recipe.presentation.ui.recipe.RecipeDestination
import com.me.recipe.presentation.ui.recipe.RecipeScreen
import com.me.recipe.presentation.ui.recipe_list.RecipeListDestination
import com.me.recipe.presentation.ui.recipe_list.RecipeListScreen
import com.me.recipe.presentation.ui.splash.SplashDestination
import com.me.recipe.presentation.ui.splash.SplashScreen
import timber.log.Timber

/**
 * Provides Navigation graph for the application.
 */
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
                    navController.navigate(RecipeListDestination.route) {
                        popUpTo(SplashDestination.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = RecipeListDestination.route) {
            RecipeListScreen(
                navigateToRecipePage = {
                    navController.navigate("${RecipeDestination.route}/${it}")
                }
            )
        }
        composable(
            route = RecipeDestination.routeWithArgs,
            arguments = listOf(
                navArgument(RecipeDestination.itemIdArg) { type = NavType.IntType }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = RecipeDestination.deeplinkWithArgs }
            )
        ) {
            RecipeScreen()
        }
    }
}
