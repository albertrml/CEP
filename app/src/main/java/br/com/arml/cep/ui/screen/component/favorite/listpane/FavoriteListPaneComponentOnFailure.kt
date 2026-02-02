package br.com.arml.cep.ui.screen.component.favorite.listpane

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FavoriteListPaneComponentOnFailure(
    modifier: Modifier = Modifier,
    failureMessage: String
){
    Text(
        modifier = modifier,
        text = failureMessage,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodyLarge
    )
}