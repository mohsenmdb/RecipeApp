package com.me.recipe.presentation.ui.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.presentation.BaseApplication
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RecipeTheme(application.isDarkTheme.value) {
                    PageContent()
                }
            }
        }
    }

    @Composable
    fun PageContent() {
        val animationState = remember { mutableStateOf(false) }

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            Rocket(
                isRocketEnabled = animationState.value,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }
        LaunchedEffect(key1 = animationState) {
            delay(1000)
            animationState.value = true
            delay(2000)
            findNavController().navigate(R.id.action_splash_to_recipeList)
        }

    }


    @Composable
    fun Rocket(
        isRocketEnabled: Boolean,
        maxWidth: Dp,
        maxHeight: Dp
    ) {
        val resource: Painter
        val modifier: Modifier
        val rocketSize = 200.dp
        if (!isRocketEnabled) {
            resource = painterResource(id = R.drawable.rocket_intial)
            modifier = Modifier.offset(
                y = maxHeight - rocketSize,
            )
        } else {
            val infiniteTransition = rememberInfiniteTransition()
            val engineState = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 500,
                        easing = LinearEasing
                    )
                )
            )
            val xPositionState = infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 2100,
                        easing = LinearEasing
                    )
                )
            )
            resource = if (engineState.value <= .5f) {
                painterResource(id = R.drawable.rocket1)
            } else {
                painterResource(id = R.drawable.rocket2)
            }
            modifier = Modifier.offset(
                x = (maxWidth - rocketSize) * xPositionState.value,
                y = (maxHeight - rocketSize) - (maxHeight) * xPositionState.value,
            )
        }
        Image(
            modifier = modifier
                .width(rocketSize)
                .height(rocketSize),
            painter = resource,
            contentDescription = "A Rocket",
        )

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        RecipeTheme {
            PageContent()
        }
    }
}
