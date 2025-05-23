package br.com.arml.cep.ui.screen.display

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.entity.Address
import br.com.arml.cep.model.mock.mockAddress
import br.com.arml.cep.ui.screen.component.common.Header
import br.com.arml.cep.ui.screen.component.display.DisplayScreenLandscape
import br.com.arml.cep.ui.screen.component.display.DisplayScreenPortrait
import br.com.arml.cep.ui.theme.PreviewInLandscape
import br.com.arml.cep.ui.theme.ShowScreenByOrientation
import br.com.arml.cep.ui.theme.dimens

@Composable
fun DisplayScreen(
    modifier: Modifier = Modifier,
    address: Address
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(
            modifier = Modifier,
            backImgVec = Icons.AutoMirrored.Filled.ArrowBack,
            title = stringResource(R.string.display_address_title)
        )

        ShowScreenByOrientation(
            portrait = {
                DisplayScreenPortrait(
                    modifier = modifier,
                    address = address
                )
            },
            landscape = {
                DisplayScreenLandscape(
                    modifier = modifier,
                    address = address
                )
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
@Composable
fun DisplayScreenPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimens.mediumPadding),
        contentAlignment = Alignment.Center
    ){
        DisplayScreen(
            modifier = Modifier,
            address = mockAddress
        )
    }
}


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
fun DisplayScreenPreviewInLandscape(){
    PreviewInLandscape {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.dimens.mediumPadding),
            contentAlignment = Alignment.Center
        ){
            DisplayScreen(
                modifier = Modifier,
                address = mockAddress
            )
        }
    }
}