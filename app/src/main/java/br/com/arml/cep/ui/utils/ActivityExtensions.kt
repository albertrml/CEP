package br.com.arml.cep.ui.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo

@SuppressLint("SourceLockedOrientationActivity")
fun Activity.lockOrientationForNonTablet() {
    val isTablet = resources.configuration.smallestScreenWidthDp >= 600
    if (!isTablet) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}