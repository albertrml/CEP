package br.com.arml.cep.ui.screen.component.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.R.string.favoriteDetailPaneComponent_onSuccess_testTag
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.domain.Place
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockPlaces
import br.com.arml.cep.ui.screen.component.common.address.AddressForms
import br.com.arml.cep.ui.screen.component.common.header.Header
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun SearchDetailPane(
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
                title = stringResource(R.string.display_address_title),
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
                    SearchDetailPaneOnSuccess(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.dimens.smallPadding)
                            .testTag(
                                stringResource(favoriteDetailPaneComponent_onSuccess_testTag)
                            ),
                        place = place,
                        onFavoriteClick = onFavoriteClick
                    )
                },
                loadingContent = {
                    SearchDetailPaneOnLoading(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(
                                stringResource(R.string.favoriteDetailPaneComponent_onLoading_testTag)
                            )
                    )
                },
                failureContent = { failure ->
                    SearchDetailPaneOnFailure(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag(
                                stringResource(R.string.favoriteDetailPaneComponent_onFailure_testTag)
                            ),
                        failure = failure
                    )
                }
            )
        }
    }

}

@Composable
fun SearchDetailPaneOnSuccess(
    modifier: Modifier = Modifier,
    place: Place,
    onFavoriteClick: (Place) -> Unit
) {

    val (colorIcon, textButton) = when (place.isFavorite.value) {
        true -> { Color.Red to stringResource(R.string.display_saved_entry) }
        false -> { MaterialTheme.colorScheme.onPrimary to stringResource(R.string.display_not_save_entry) }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Button(
            modifier = Modifier
                .align(Alignment.End)
                .testTag(
                    stringResource(
                        R.string.favoriteDetailPaneComponent_saveButton_testTag
                    )
                ),
            enabled = !place.isFavorite.value,
            onClick = { onFavoriteClick(place) }
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = stringResource(R.string.display_favorite_button_description),
                tint = colorIcon
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallPadding))
            Text(
                text = textButton,
                style = MaterialTheme.typography.titleMedium
            )
        }
        AddressForms(address = place.address)
    }
}

@Composable
fun SearchDetailPaneOnLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .testTag(stringResource(R.string.favoriteDetailPaneComponent_circularProgressIndicator_testTag))
        )
    }
}

@Composable
fun SearchDetailPaneOnFailure(
    modifier: Modifier = Modifier,
    failure: Exception
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.titleLarge,
            text = failure.message ?: CepException.NotFoundCepException().message
        )
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
    SearchDetailPane(
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
    SearchDetailPane(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.mediumMargin),
        response = Response.Success(place),
        onBackPress = {},
        onFavoriteClick = {}
    )
}