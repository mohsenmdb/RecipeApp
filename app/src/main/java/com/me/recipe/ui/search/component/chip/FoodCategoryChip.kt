package com.me.recipe.ui.search.component.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun FoodCategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AssistChip(
        modifier = modifier,
        shape = CircleShape,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        onClick = {
            onSelectedCategoryChanged(category)
            onClick()
        },
        border = isSelected.takeIf { it }?.let {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
            )
        },
        label = {
            Text(
                text = category,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
    )
}

@Preview
@Composable
private fun FoodCategoryChipPreview() {
    RecipeTheme(true) {
        FoodCategoryChip(
            category = "Chips",
            isSelected = true,
            onClick = {},
            onSelectedCategoryChanged = {},
        )
    }
}

@Preview
@Composable
private fun FoodCategoryChipPreview2() {
    RecipeTheme(false) {
        FoodCategoryChip(
            category = "Chips",
            isSelected = false,
            onClick = {},
            onSelectedCategoryChanged = {},
        )
    }
}
