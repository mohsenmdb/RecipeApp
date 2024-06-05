package com.me.recipe.ui.recipelist.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RowScope.MoreButton(onToggleTheme: () -> Unit) {
    IconButton(
        modifier = Modifier
            .align(Alignment.CenterVertically)
            .testTag("testTag_MoreButton"),
        onClick = { onToggleTheme() },
    ) {
        Icon(Icons.Filled.MoreVert, "")
    }
}

@Preview
@Composable
private fun LoadingMoreButton() {
    RecipeTheme(true) {
        Row {
            MoreButton(onToggleTheme = {})
        }
    }
}
