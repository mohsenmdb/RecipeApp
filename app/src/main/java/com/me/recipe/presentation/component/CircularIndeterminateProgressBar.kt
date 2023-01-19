package com.me.recipe.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun CircularIndeterminateProgressBar(isVisible: Boolean) {
    if (isVisible) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (progressBar, text) = createRefs()
            val guideline = createGuidelineFromTop(0.02f)
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(guideline)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                color = MaterialTheme.colors.primary
            )

            Text(
                text = "Loading...",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(top = 8.dp).constrainAs(text) {
                    top.linkTo(progressBar.bottom)
                    start.linkTo(progressBar.start)
                    end.linkTo(progressBar.end)
                }
            )
        }
    }
}