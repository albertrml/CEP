package br.com.arml.cep.application

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun HideStatusBarSystem(
    content: @Composable () -> Unit
){

    val view = LocalView.current

    if (!view.isInEditMode) {
        val currentWindow = (view.context as? Activity)?.window ?: return

        SideEffect {
            // Draw behind the status bar
            WindowCompat.setDecorFitsSystemWindows(currentWindow, false)
            // Make the status bar transparent
            //currentWindow.statusBarColor = Color.Transparent.hashCode()
            view.setBackgroundColor(Color.Transparent.hashCode())

            // Access WindowInsetsController for finer control
            val windowInsetsController = WindowCompat.getInsetsController(currentWindow, view)

            // Hide the status bar
            windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())

            // Optional: Control the behavior of the status bar when it reappears (e.g., after a swipe)
            windowInsetsController.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

            // Optional: Set the status bar icons to be light or dark
            windowInsetsController.isAppearanceLightStatusBars =
                true // Set to true if your background is dark, false if it's light
        }
    }

    content()

}