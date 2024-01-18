package com.me.recipe.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.me.recipe.R
import com.me.recipe.presentation.ui.navigation.ComingSoonDestination
import com.me.recipe.presentation.ui.navigation.RecipeListDestination
import com.me.recipe.presentation.ui.navigation.RecipeNavHost
import com.me.recipe.presentation.ui.navigation.SplashDestination
import com.me.recipe.presentation.ui.navigation.navigateSingleTopTo


@Composable
fun RecipeApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            AnimatedVisibility(
                visible = currentDestination != null && currentDestination.route != SplashDestination.route,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                BottomNavigation {
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(R.string.recipes)) },
                        selected = currentDestination?.hierarchy?.any { it.route == RecipeListDestination.route } == true,
                        onClick = {
                            navController.navigateSingleTopTo(RecipeListDestination.route)
                        }
                    )
                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.Face, contentDescription = null) },
                        label = { Text(stringResource(R.string.coming)) },
                        selected = currentDestination?.hierarchy?.any { it.route == ComingSoonDestination.route } == true,
                        onClick = {
                            navController.navigateSingleTopTo(ComingSoonDestination.route)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        RecipeNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
