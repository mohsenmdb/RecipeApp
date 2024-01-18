package com.me.recipe.presentation.ui.coming_soon

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ComingSoonScreen(
    modifier: Modifier = Modifier
) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = "Coming Soon", style = MaterialTheme.typography.titleLarge)
    }
}