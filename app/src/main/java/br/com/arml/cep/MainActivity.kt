package br.com.arml.cep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import br.com.arml.cep.application.HideStatusBarSystem
import br.com.arml.cep.ui.navigation.CepNavigation
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
                    CepNavigation(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}