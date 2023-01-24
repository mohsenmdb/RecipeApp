package com.me.recipe.presentation.ui.recipe

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FoodCategoryChip(
    category: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit
) {
    Surface(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.Magenta else MaterialTheme.colors.primary,
        modifier = Modifier
            .padding(end = 8.dp)
            .toggleable(value = isSelected, onValueChange = {
                onSelectedCategoryChanged(category)
                onClick()
            })
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(8.dp),
            color = Color.White
        )
    }
}