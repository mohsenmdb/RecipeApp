package com.me.recipe.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.me.recipe.ui.navigation.NavBottomBar
import com.me.recipe.ui.navigation.RecipeNavHost

@Composable
internal fun RecipeApp(navController: NavHostController = rememberNavController()) {
    Scaffold(
        bottomBar = { NavBottomBar(navController) },
    ) { innerPadding ->
        RecipeNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}
