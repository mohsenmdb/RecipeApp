package com.me.recipe.ui.recipelist.component.chip

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    Surface(
        shadowElevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.Magenta else MaterialTheme.colorScheme.primary,
        modifier = modifier
            .toggleable(value = isSelected, onValueChange = {
                onSelectedCategoryChanged(category)
                onClick()
            }),
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp),
            color = Color.White,
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
