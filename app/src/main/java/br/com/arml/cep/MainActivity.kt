@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package br.com.arml.cep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.ui.Modifier
import br.com.arml.cep.application.HideStatusBarSystem
import br.com.arml.cep.ui.screen.cep.CepScreen
import br.com.arml.cep.ui.theme.CEPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HideStatusBarSystem {
                CEPTheme {
                    Surface {
                        CepScreen(
                            modifier = Modifier
                                .windowInsetsPadding(WindowInsets.systemBars)
                                .windowInsetsPadding(WindowInsets.displayCutout)
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .windowInsetsPadding(WindowInsets.systemGestures)
                        )
                    }
                }
            }
        }
    }
}