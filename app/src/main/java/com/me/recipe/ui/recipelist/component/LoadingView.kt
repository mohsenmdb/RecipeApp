package com.me.recipe.ui.recipelist.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.me.recipe.R
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun LoadingView(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
) {
    if (isVisible) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (progressBar, text) = createRefs()
            val guideline = createGuidelineFromTop(0.02f)
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(guideline)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = MaterialTheme.colorScheme.primary,
            )

            Text(
                text = stringResource(R.string.loading),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .constrainAs(text) {
                        top.linkTo(progressBar.bottom)
                        start.linkTo(progressBar.start)
                        end.linkTo(progressBar.end)
                    },
            )
        }
    }
}

@Preview
@Composable
private fun LoadingViewPreview() {
    RecipeTheme(true) {
        LoadingView(isVisible = true)
    }
}
