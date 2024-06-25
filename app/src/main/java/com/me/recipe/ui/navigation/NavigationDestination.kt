package com.me.recipe.ui.navigation

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.me.recipe.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object SplashDestination : NavigationDestination {
    override val route = "splash"
    override val titleRes = R.string.navigate_splash_title
}

object RecipeListDestination : NavigationDestination {
    override val route = "RecipeList"
    override val titleRes = R.string.navigate_recipe_list_title
}

object RecipeDestination : NavigationDestination {
    override val route = "Recipe"
    override val titleRes = R.string.navigate_recipe_title
    const val ITEM_ID_ARG = "itemId"
    const val ITEM_TITLE_ARG = "itemTitle"
    const val ITEM_IMAGE_ARG = "itemImage"
    val routeWithArgs = "$route/{$ITEM_ID_ARG}/{$ITEM_TITLE_ARG}/{$ITEM_IMAGE_ARG}"
    val arguments = listOf(
        navArgument(ITEM_ID_ARG) { type = NavType.IntType },
        navArgument(ITEM_TITLE_ARG) { type = NavType.StringType },
        navArgument(ITEM_IMAGE_ARG) { type = NavType.StringType },
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "recipe://composables.com/{$ITEM_ID_ARG}/{$ITEM_TITLE_ARG}/{$ITEM_IMAGE_ARG}" },
    )
}

object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val titleRes = R.string.navigate_home_title
}

val bottomNavigationScreens = listOf(RecipeListDestination, HomeDestination)

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        // we can use navController.graph.findStartDestination().id instead of RecipeListDestination.route (without splash)
        popUpTo(
            RecipeListDestination.route,
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
fun NavHostController.navigateSingleTopFromSplash(route: String) =
    this.navigate(route) {
        popUpTo(SplashDestination.route) {
            inclusive = true
        }
        launchSingleTop = true
        restoreState = true
    }
