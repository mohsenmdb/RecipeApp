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
import com.me.recipe.ui.home.HomeScreen
import com.me.recipe.ui.recipe.RecipeScreen
import com.me.recipe.ui.recipelist.RecipeListScreen
import com.me.recipe.ui.splash.SplashScreen

@Composable
internal fun RecipeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = SplashDestination.route,
            modifier = modifier,
        ) {
            composable(route = SplashDestination.route) {
                SplashScreen(
                    navigateToRecipeList = {
                        navController.navigateSingleTopFromSplash(RecipeListDestination.route)
                    },
                )
            }
            composable(route = RecipeListDestination.route) {
                RecipeListScreen(
                    navigateToRecipePage = { id, title, image ->
                        navController.navigateSingleTopTo("${RecipeDestination.route}/$id/$title/$image")
                    },
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
                    navigateToRecipePage = { id, title, image ->
                        navController.navigateSingleTopTo("${RecipeDestination.route}/$id/$title/$image")
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                )
            }
        }
    }
}
