package com.me.recipe.presentation.component.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.me.recipe.R

@Composable
internal fun CoilImage(
    data: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    contentDescription: String? = null,
    @DrawableRes placeholder: Int? = R.drawable.empty_plate,
    @DrawableRes errorImage: Int? = R.drawable.empty_plate,
) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(data)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        contentScale = contentScale,
        error = if (errorImage != null) painterResource(id = errorImage) else null,
        placeholder = if (placeholder != null) painterResource(id = placeholder) else null,
    )
}
