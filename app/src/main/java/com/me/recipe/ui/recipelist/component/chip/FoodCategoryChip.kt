package com.me.recipe.ui.recipelist.component.chip

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.ifElse

@Composable
internal fun FoodCategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        shadowElevation = 4.dp,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .ifElse(
                condition = isSelected,
                ifTrueModifier = Modifier.border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = CircleShape,
                ),
            )
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectedCategoryChanged(category)
                    onClick()
                },
            ),
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
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
