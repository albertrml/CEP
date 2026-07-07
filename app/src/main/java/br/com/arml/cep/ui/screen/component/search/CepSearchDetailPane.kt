package br.com.arml.cep.ui.screen.component.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import br.com.arml.core.response.Response
import br.com.arml.cep.model.mock.mockPlaces
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.screen.component.search.detailpane.CepSearchDetailPaneOnFailure
import br.com.arml.cep.ui.screen.component.search.detailpane.CepSearchDetailPaneOnLoading
import br.com.arml.cep.ui.screen.component.search.detailpane.CepSearchDetailPaneOnSuccess
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.core.response.ui.ShowResults

@Composable
fun CepSearchDetailPane(
    modifier: Modifier = Modifier,
    response: Response<Place>,
    onBackPress: () -> Unit,
    onFavoriteClick: (Place) -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            Header(
                logo = Icons.AutoMirrored.Filled.ArrowBack,
                title = stringResource(R.string.searchDetailPane_header_label),
                onClickLogo = onBackPress,
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            response.ShowResults(
                successContent = { place ->
                    CepSearchDetailPaneOnSuccess(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.dimens.smallPadding),
                        place = place,
                        onFavoriteClick = onFavoriteClick
                    )
                },
                loadingContent = {
                    CepSearchDetailPaneOnLoading(
                        modifier = Modifier.fillMaxSize()
                    )
                },
                failureContent = { failure ->
                    CepSearchDetailPaneOnFailure(
                        modifier = Modifier.fillMaxSize(),
                        failure = failure
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
@Preview(
    name = "Smart Phone Landscape",
    showBackground = true,
    device = "spec:width=640dp,height=360dp"
)
@Preview(
    name = "Compact Phone Landscape",
    showBackground = true,
    device = "spec:width=924dp,height=412dp"
)
@Preview(
    name = "Medium Tablet Landscape",
    showBackground = true,
    device = "spec:width=1280dp,height=800dp"
)
@Composable
fun DisplayScreenPreview() {
    val place = mockPlaces(1,false).first()
    CepSearchDetailPane(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.mediumMargin),
        response = Response.Success(place),
        onBackPress = {},
        onFavoriteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun DisplayScreenWithFavoritePreview() {
    val place = mockPlaces(1,true).first()
    CepSearchDetailPane(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.mediumMargin),
        response = Response.Success(place),
        onBackPress = {},
        onFavoriteClick = {}
    )
}