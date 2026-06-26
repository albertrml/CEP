package br.com.arml.cep.ui.screen.component.search.listpane

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SearchComponent(
    modifier: Modifier = Modifier,
    selectedTab: SearchTab,
    onSearchCep: (String) -> Unit = {},
    onSearchAddress: (String, String, String) -> Unit = { _, _, _ -> }
){
    when(selectedTab){
        is SearchTab.Address -> {
            AddressSearchComponent(
                modifier = modifier,
                onSearchAddress = onSearchAddress
            )
        }
        is SearchTab.CEP -> {
            CepSearchComponent(
                modifier = modifier,
                onSearchCep = onSearchCep
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchComponentOnCepPreview(){
    SearchComponent(
        modifier = Modifier,
        selectedTab = SearchTab.CEP
    )
}

@Preview(showBackground = true)
@Composable
fun SearchComponentOnAddressPreview(){
    SearchComponent(
        modifier = Modifier,
        selectedTab = SearchTab.Address
    )
}