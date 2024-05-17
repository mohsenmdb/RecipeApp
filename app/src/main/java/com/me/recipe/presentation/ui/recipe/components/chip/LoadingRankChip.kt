package com.me.recipe.presentation.ui.recipe.components.chip

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun LoadingRankChip(
    modifier: Modifier = Modifier,
) {
    val alpha by rememberInfiniteTransition(label = "").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 500
                0.2f at 0
                0.7f at 250
            },
            repeatMode = RepeatMode.Reverse,
        ),
        label = "alpha",
    )
    AssistChip(
        shape = CircleShape,
        modifier = modifier.alpha(alpha),
        onClick = { },
        label = {
            Text(
                text = "0",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray,
            )
        },
        leadingIcon = {
            Icon(
                Icons.Filled.ThumbUp,
                contentDescription = "ThumbUp Icon",
                Modifier.size(AssistChipDefaults.IconSize),
                tint = Color.Gray,
            )
        },
    )
}

@Preview
@Composable
private fun LoadingRankChipPreview() {
    RecipeTheme(true) {
        LoadingRankChip()
    }
}
