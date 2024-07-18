package com.me.recipe.ui.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.compose.OnClick

@Composable
internal fun HomeAppBar(
    isDark: Boolean,
    onToggleTheme: OnClick,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
            )
            Icon(
                painter = painterResource(id = if (isDark)R.drawable.ic_light_mode_24 else R.drawable.ic_night_mode_24),
                contentDescription = stringResource(id = R.string.app_name),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    onToggleTheme.invoke()
                },
            )
        }
    }
}

@Preview
@Composable
private fun HomeAppBarPreview() {
    RecipeTheme(false) {
        HomeAppBar(
            isDark = false,
            onToggleTheme = {},
        )
    }
}

@Preview
@Composable
private fun HomeAppBarDarkPreview() {
    RecipeTheme(true) {
        HomeAppBar(
            isDark = true,
            onToggleTheme = {},
        )
    }
}
