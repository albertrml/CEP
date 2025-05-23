package br.com.arml.cep.ui.screen.component.display

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AddressMap(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxSize()
    ){
        Text(
            text = "Map",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}