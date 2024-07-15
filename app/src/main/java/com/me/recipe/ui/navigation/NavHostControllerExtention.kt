package com.me.recipe.ui.navigation

import androidx.navigation.NavHostController

fun NavHostController.navigateTo(route: String) =
    this.navigate(route)

fun NavHostController.navigateSingleTopTo(route: String) =
    navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        // we can use graph.findStartDestination().id instead of RecipeListDestination.route (without splash)
        popUpTo(HomeDestination.route) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }

fun NavHostController.navigateSingleTopFromSplash(route: String) =
    navigate(route) {
        popUpTo(SplashDestination.route) {
            inclusive = true
        }
        launchSingleTop = true
        restoreState = true
    }
