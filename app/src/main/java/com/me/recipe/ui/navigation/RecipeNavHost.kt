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
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.home.HomeScreen
import com.me.recipe.ui.recipe.RecipeScreen
import com.me.recipe.ui.recipelist.RecipeListScreen
import com.me.recipe.ui.search.SearchScreen
import com.me.recipe.ui.splash.SplashScreen
import com.me.recipe.util.extention.encodeToUtf8

@Composable
internal fun RecipeNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    fun navigateToRecipePage(recipe: Recipe) {
        navController.navigateTo("${RecipeDestination.route}/${recipe.id}/${recipe.title}/${recipe.featuredImage.encodeToUtf8()}/${recipe.uid}")
    }
    fun navigateToRecipeListPage(category: FoodCategory) {
        navController.navigateTo("${RecipeListDestination.route}/${category.value}")
    }

    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = SplashDestination.route,
            modifier = modifier,
        ) {
            composable(route = SplashDestination.route) {
                SplashScreen(
                    navigateToHome = {
                        navController.navigateSingleTopFromSplash(HomeDestination.route)
                    },
                )
            }
            composable(route = HomeDestination.route) {
                HomeScreen(
                    navigateToRecipePage = { navigateToRecipePage(it) },
                    navigateToRecipeListPage = { navigateToRecipeListPage(it) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                )
            }
            composable(route = SearchDestination.route) {
                SearchScreen(
                    navigateToRecipePage = { navigateToRecipePage(it) },
                    navigateToHomePage = { navController.navigateSingleTopTo(HomeDestination.route) },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                )
            }
            composable(
                route = RecipeListDestination.routeWithArgs,
                arguments = RecipeListDestination.arguments,
            ) {
                RecipeListScreen(
                    navigateToRecipePage = { navigateToRecipePage(it) },
                    onBackPress = { navController.popBackStack() },
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
        }
    }
}
