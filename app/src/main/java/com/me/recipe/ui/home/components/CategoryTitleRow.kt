package com.me.recipe.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.shared.utils.FoodCategory

@Composable
internal fun CategoryTitleRow(
    category: FoodCategory,
    onCategoryClicked: (FoodCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 16.dp, bottom = 12.dp),
    ) {
        Text(
            text = category.value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = stringResource(id = R.string.see_all),
            modifier = Modifier
                .clickable {
                    onCategoryClicked.invoke(category)
                },
        )
    }
}

@Preview
@Composable
private fun CategoryTitleRowPreview() {
    CategoryTitleRow(category = FoodCategory.CHICKEN, onCategoryClicked = {})
}
