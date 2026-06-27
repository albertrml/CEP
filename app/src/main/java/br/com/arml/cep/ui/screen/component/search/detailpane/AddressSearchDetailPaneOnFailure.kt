package br.com.arml.cep.ui.screen.component.search.detailpane

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AddressSearchDetailPaneOnFailure(
    modifier: Modifier = Modifier,
    failureMsg: String
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Text(text = failureMsg)
    }
}