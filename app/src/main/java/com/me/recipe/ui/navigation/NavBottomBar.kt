package com.me.recipe.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.me.recipe.R

@Composable
internal fun NavBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val itemColors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.tertiary,
        selectedIconColor = MaterialTheme.colorScheme.surface,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    AnimatedVisibility(
        visible = bottomNavigationScreens.any { it.route == currentDestination?.route },
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                label = { Text(stringResource(R.string.home)) },
                selected = currentDestination?.route == HomeDestination.route,
                colors = itemColors,
                onClick = {
                    navController.navigateSingleTopTo(HomeDestination.route)
                },
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Search, contentDescription = null) },
                label = { Text(stringResource(R.string.search)) },
                selected = currentDestination?.route == SearchDestination.route,
                colors = itemColors,
                onClick = {
                    navController.navigateSingleTopTo(SearchDestination.route)
                },
            )
        }
    }
}
