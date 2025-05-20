package br.com.arml.cep.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable

@Composable
fun ShowScreen(
    compact: @Composable () -> Unit,
    medium: @Composable () -> Unit,
    expanded: @Composable () -> Unit
){
    when(MaterialTheme.currentWindowWidthSize){
        WindowWidthSizeClass.Compact -> compact()
        WindowWidthSizeClass.Medium -> medium()
        else -> expanded()
    }
}