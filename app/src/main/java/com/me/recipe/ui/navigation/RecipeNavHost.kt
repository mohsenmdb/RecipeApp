@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.home.HomeScreen
import com.me.recipe.ui.recipe.RecipeScreen
import com.me.recipe.ui.recipelist.RecipeListScreen
import com.me.recipe.ui.splash.SplashScreen
import com.me.recipe.util.extention.encodeToUtf8

@Composable
internal fun RecipeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    fun navigateToRecipePage(recipe: Recipe, startDestination: String) {
        navController.navigateTo("${RecipeDestination.route}/${recipe.id}/${recipe.title}/${recipe.featuredImage.encodeToUtf8()}/$startDestination")
    }

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = SplashDestination.route,
            modifier = modifier,
        ) {
            composable(route = SplashDestination.route) {
                SplashScreen(
                    navigateToRecipeList = {
                        navController.navigateSingleTopFromSplash(HomeDestination.route)
                    },
                )
            }
            composable(route = RecipeListDestination.route) {
                RecipeListScreen(
                    navigateToRecipePage = { navigateToRecipePage(it, RecipeListDestination.route) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                )
            }
            composable(
                route = RecipeDestination.routeWithArgs,
                arguments = RecipeDestination.arguments,
                deepLinks = RecipeDestination.deepLinks,
            ) {
                RecipeScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                )
            }
            composable(route = HomeDestination.route) {
                HomeScreen(
                    navigateToRecipePage = { navigateToRecipePage(it, HomeDestination.route) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                )
            }
        }
    }
}
