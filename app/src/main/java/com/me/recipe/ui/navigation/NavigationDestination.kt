package com.me.recipe.ui.navigation

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

object HomeDestination : NavigationDestination {
    override val route = "Home"
    override val titleRes = R.string.navigate_home_title
}

object ScreenDestination : NavigationDestination {
    override val route = "Screen"
    override val titleRes = R.string.navigate_search_title
}

object RecipeDestination : NavigationDestination {
    override val route = "Recipe"
    override val titleRes = R.string.navigate_recipe_title
    const val ITEM_ID_ARG = "itemId"
    const val ITEM_TITLE_ARG = "itemTitle"
    const val ITEM_IMAGE_ARG = "itemImage"
    const val ITEM_UID_ARG = "itemUid"
    val routeWithArgs =
        "$route/{$ITEM_ID_ARG}/{$ITEM_TITLE_ARG}/{$ITEM_IMAGE_ARG}/{$ITEM_UID_ARG}"
    val arguments = listOf(
        navArgument(ITEM_ID_ARG) { type = NavType.IntType },
        navArgument(ITEM_TITLE_ARG) { type = NavType.StringType },
        navArgument(ITEM_IMAGE_ARG) { type = NavType.StringType },
        navArgument(ITEM_UID_ARG) {
            type = NavType.StringType
            nullable = true
            defaultValue = ""
        },
    )
    val deepLinks = listOf(
        navDeepLink {
            uriPattern =
                "recipe://composables.com/{$ITEM_ID_ARG}/{$ITEM_TITLE_ARG}/{$ITEM_IMAGE_ARG}"
        },
    )
}
