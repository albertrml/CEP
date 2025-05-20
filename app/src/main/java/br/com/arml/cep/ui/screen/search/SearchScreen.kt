package br.com.arml.cep.ui.screen.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import br.com.arml.cep.ui.screen.common.calculatePadding

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
){
    Scaffold(modifier = modifier) { innerPadding ->
        Greeting(
            name = "Android",
            modifier = Modifier.padding(calculatePadding(innerPadding))
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiary,
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold
    )
}