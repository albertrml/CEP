package br.com.arml.cep.ui.theme

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun ShowScreenByOrientation(
    portrait: @Composable () -> Unit,
    landscape: @Composable () -> Unit
){
    when(MaterialTheme.currentScreenOrientation){
        Configuration.ORIENTATION_PORTRAIT -> portrait()
        Configuration.ORIENTATION_LANDSCAPE -> landscape()
        else -> portrait()
    }
}

@Composable
fun PreviewInLandscape(content: @Composable () -> Unit) {
    val landscapeConfiguration = Configuration(LocalConfiguration.current)
    landscapeConfiguration.setToDefaults()
    landscapeConfiguration.orientation = Configuration.ORIENTATION_LANDSCAPE

    CEPTheme {
        CompositionLocalProvider(LocalConfiguration provides landscapeConfiguration) {
            content()
        }
    }
}