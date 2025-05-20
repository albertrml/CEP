package br.com.arml.cep.ui.theme

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf

val LocalAppDimens = compositionLocalOf { compactDimens }
val LocalWindowWidthSizeClass = compositionLocalOf { WindowWidthSizeClass.Compact }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CEPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity? = LocalActivity.current,
    content: @Composable () -> Unit
) {

    val appWidthSizeClass = activity?.let {
        calculateWindowSizeClass(it).widthSizeClass
    } ?: WindowWidthSizeClass.Compact

    val (appDimens, appTypography) = getDimensAndTypographyByWindowsSize(appWidthSizeClass)

    CompositionLocalProvider(
        LocalAppDimens provides appDimens,
        LocalWindowWidthSizeClass provides appWidthSizeClass
    ) {
        MaterialTheme(
            colorScheme = getColorScheme(darkTheme = darkTheme),
            typography = appTypography,
            content = content
        )
    }
}
val MaterialTheme.currentWindowWidthSize
    @Composable
    get() = LocalWindowWidthSizeClass.current

val MaterialTheme.dimens
    @Composable
    get() = LocalAppDimens.current