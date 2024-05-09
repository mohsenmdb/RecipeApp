package com.me.recipe.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.me.recipe.R
import com.me.recipe.presentation.ui.navigation.ComingSoonDestination
import com.me.recipe.presentation.ui.navigation.RecipeListDestination
import com.me.recipe.presentation.ui.navigation.RecipeNavHost
import com.me.recipe.presentation.ui.navigation.bottomNavigationScreens
import com.me.recipe.presentation.ui.navigation.navigateSingleTopTo

@Composable
fun RecipeApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            AnimatedVisibility(
                visible = bottomNavigationScreens.any { it.route == currentDestination?.route },
                enter = expandVertically(),
                exit = shrinkVertically(),
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                        label = { Text(stringResource(R.string.recipes)) },
                        selected = currentDestination?.route == RecipeListDestination.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        onClick = {
                            navController.navigateSingleTopTo(RecipeListDestination.route)
                        },
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Face, contentDescription = null) },
                        label = { Text(stringResource(R.string.coming)) },
                        selected = currentDestination?.route == ComingSoonDestination.route,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSurface,
                            selectedTextColor = MaterialTheme.colorScheme.onSurface,
                            indicatorColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        onClick = {
                            navController.navigateSingleTopTo(ComingSoonDestination.route)
                        },
                    )
                }
            }
        },
    ) { innerPadding ->
        RecipeNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
