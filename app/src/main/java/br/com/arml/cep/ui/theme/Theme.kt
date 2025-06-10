package br.com.arml.cep.ui.theme

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import br.com.arml.cep.MainActivity

val LocalAppDimens = compositionLocalOf { compactDimens }

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun CEPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    activity: Activity = LocalActivity.current as MainActivity,
    content: @Composable () -> Unit
) {
    val appOrientation = LocalConfiguration.current.orientation
    val (appDimens, appTypography) = getDimensAndTypographyByScreenOrientation(
        screenOrientation = appOrientation,
        windowSizeClass = calculateWindowSizeClass(activity)
    )
    CompositionLocalProvider(
        LocalAppDimens provides appDimens,
    ) {
        MaterialTheme(
            colorScheme = getColorScheme(darkTheme = darkTheme),
            typography = appTypography,
            content = content
        )
    }
}

val MaterialTheme.dimens
    @Composable get() = LocalAppDimens.current