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
import com.me.recipe.presentation.ui.navigation.NavBottomBar
import com.me.recipe.presentation.ui.navigation.RecipeListDestination
import com.me.recipe.presentation.ui.navigation.RecipeNavHost
import com.me.recipe.presentation.ui.navigation.bottomNavigationScreens
import com.me.recipe.presentation.ui.navigation.navigateSingleTopTo

@Composable
fun RecipeApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { NavBottomBar(navController) },
    ) { innerPadding ->
        RecipeNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

