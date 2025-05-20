package br.com.arml.cep.ui.screen.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        modifier = modifier
    )
}