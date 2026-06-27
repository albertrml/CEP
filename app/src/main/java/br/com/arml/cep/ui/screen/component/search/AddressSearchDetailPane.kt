package br.com.arml.cep.ui.screen.component.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.mock.mockPlaces
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.screen.component.search.detailpane.AddressSearchDetailPaneOnFailure
import br.com.arml.cep.ui.screen.component.search.detailpane.AddressSearchDetailPaneOnLoading
import br.com.arml.cep.ui.screen.component.search.detailpane.AddressSearchDetailPaneOnSuccess
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun AddressSearchDetailPane(
    modifier: Modifier = Modifier,
    response: Response<List<Place>>,
    onNavigateToAddress: (Place) -> Unit = {},
    onBackPress: () -> Unit = {}
){
    Scaffold(
        modifier = modifier,
        topBar = {
            Header(
                logo = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(R.string.searchDetailPane_header_label),
                onClickLogo = onBackPress,
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing)
        ) {
            Log.d("AddressSearchDetailPane", "AddressSearchDetailPane: ${response}")
            response.ShowResults(
                successContent = {
                    AddressSearchDetailPaneOnSuccess(
                        places = it,
                        onNavigateToAddress = onNavigateToAddress
                    )
                },
                loadingContent = {
                    AddressSearchDetailPaneOnLoading(
                        modifier = Modifier.fillMaxSize()
                    )
                },
                failureContent = {
                    AddressSearchDetailPaneOnFailure(
                        modifier = Modifier.fillMaxSize(),
                        failureMsg = it.message ?: "Falha desconhecida"
                    )
                }
            )
        }
    }
}


@Preview(
    name = "Smart Phone Portrait",
    showBackground = true,
    device = "spec:width=360dp,height=640dp"
)
@Preview(
    name = "Compact Phone Portrait",
    showBackground = true,
    device = "spec:width=412dp,height=924dp"
)
@Preview(
    name = "Medium Tablet Portrait",
    showBackground = true,
    device = "spec:width=800dp,height=1280dp"
)
@Composable
fun AddressSearchDetailPanePreview(){
    val response = Response.Success(mockPlaces(5,false))
    AddressSearchDetailPane(
        response = response
    )
}