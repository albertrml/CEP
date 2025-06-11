package br.com.arml.cep.ui.screen.component.cep.display

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Response
import br.com.arml.cep.model.entity.PlaceEntry
import br.com.arml.cep.model.exception.CepException
import br.com.arml.cep.model.mock.mockPlaceEntries
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.theme.dimens
import br.com.arml.cep.ui.utils.ShowResults

@Composable
fun DisplayScreen(
    modifier: Modifier = Modifier,
    response: Response<PlaceEntry>,
    onBackPress: () -> Unit,
    onFavoriteClick: (PlaceEntry) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            modifier = Modifier,
            backImgVec = Icons.AutoMirrored.Filled.ArrowBack,
            title = stringResource(R.string.display_address_title),
            onBackClick = onBackPress
        )

        response.ShowResults(
            successContent = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AddressScreen(
                        modifier = Modifier.padding(
                            vertical = MaterialTheme.dimens.largeMargin
                        ),
                        address = it.address
                    )

                    Button(
                        enabled = !it.isFavorite.value,
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = { onFavoriteClick(it) }
                    ) {

                        val (colorIcon, textButton) = when (it.isFavorite.value) {
                            true -> {
                                Color.Red to
                                        stringResource(R.string.display_saved_entry)
                            }
                            false -> {
                                MaterialTheme.colorScheme.onPrimary to
                                        stringResource(R.string.display_not_save_entry)
                            }
                        }
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = stringResource(R.string.icon_button_tag),
                            tint = colorIcon
                        )
                        Spacer(modifier = Modifier.padding(MaterialTheme.dimens.smallPadding))
                        Text(
                            text = textButton,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            },
            loadingContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.testTag(stringResource(R.string.display_loading_indicator))
                    )
                }
            },
            failureContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = it.message ?: CepException.NotFoundCepException().message
                    )
                }
            }
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
    DisplayScreen(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.mediumMargin),
        response = Response.Success(mockPlaceEntries[0]),
        onBackPress = {},
        onFavoriteClick = {}
    )
}