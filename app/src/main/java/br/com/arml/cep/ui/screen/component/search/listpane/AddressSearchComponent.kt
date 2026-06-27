package br.com.arml.cep.ui.screen.component.search.listpane

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.brazilianUF
import br.com.arml.cep.ui.screen.component.common.field.CepDropDownMenu
import br.com.arml.cep.ui.theme.dimens

@Composable
fun AddressSearchComponent(
    modifier: Modifier = Modifier,
    onSearchAddress: (String, String, String) -> Unit = { _, _, _ -> }
){

    var queryUF by rememberSaveable { mutableStateOf("") }
    var queryCity by rememberSaveable { mutableStateOf("") }
    var queryStreet by rememberSaveable { mutableStateOf("") }
    val isSearchEnable by remember {
        derivedStateOf {
            queryUF.isNotEmpty() &&
            queryCity.length >= 3 &&
            queryStreet.length >= 3
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ){
        CepDropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            value = queryUF,
            list = brazilianUF,
            label = stringResource(R.string.addressSearchComponent_uf_label),
            onItemSelected = { queryUF = it }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = queryCity,
            onValueChange = { queryCity = it },
            label = { Text(stringResource(R.string.addressSearchComponent_city_label)) },
            singleLine = true
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = queryStreet,
            onValueChange = { queryStreet = it },
            label = { Text(stringResource(R.string.addressSearchComponent_street_label)) },
            singleLine = true
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onSearchAddress(queryUF, queryCity, queryStreet) },
            enabled = isSearchEnable,
            colors = ButtonDefaults.buttonColors(
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
        ) {
            Text(text = stringResource(R.string.addressSearchComponent_searchButton_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressSearchComponentPreview(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
    ) {
        AddressSearchComponent()
    }
}
