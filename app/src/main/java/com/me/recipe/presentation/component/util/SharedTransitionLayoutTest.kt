@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.presentation.component.util

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun SharedTransitionLayoutTest(
    content: @Composable SharedTransitionScope.(animatedContentScope: AnimatedContentScope) -> Unit,
) {
    SharedTransitionLayout(Modifier.fillMaxSize()) {
        AnimatedContent(targetState = 0, label = "AnimatedContent") { targetState ->
            when (targetState) {
                0 -> {
                    content(this@AnimatedContent)
                }
            }
        }
    }
}
