package com.me.recipe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.sp
import com.me.recipe.R

val IranSans = FontFamily(
    Font(R.font.iran_sans, FontWeight.Normal),
    Font(R.font.iran_sans_medium, FontWeight.Medium),
    Font(R.font.iran_sans_bold, FontWeight.Bold),
    Font(R.font.iran_sans_light, FontWeight.Light),
    Font(R.font.iran_sans_extra_light, FontWeight.ExtraLight),
    Font(R.font.iran_sans_black, FontWeight.Black),
)

// Set of Material typography styles to start with
@OptIn(ExperimentalTextApi::class)
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = (-0.25).sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    displayMedium = TextStyle(
        fontSize = 45.sp,
        lineHeight = 52.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    displaySmall = TextStyle(
        fontSize = 36.sp,
        lineHeight = 44.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    headlineMedium = TextStyle(
        fontSize = 28.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    titleMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    titleSmall = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    labelLarge = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    labelMedium = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.25.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    labelSmall = TextStyle(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.1.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.5.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    bodyMedium = TextStyle(
        fontSize = 14.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.25.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
    bodySmall = TextStyle(
        fontSize = 12.sp,
        lineHeight = 16.sp,
        fontWeight = FontWeight.Normal,
        letterSpacing = 0.2.sp,
        fontFamily = IranSans,
        textMotion = TextMotion.Animated,
    ),
)
