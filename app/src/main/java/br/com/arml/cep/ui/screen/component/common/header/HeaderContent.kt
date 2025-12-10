package br.com.arml.cep.ui.screen.component.common.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens

@Composable
fun HeaderContent(
    modifier: Modifier = Modifier,
    title: String,
    logo: ImageVector,
    onClickLogo: () -> Unit = {},
    menu: @Composable () -> Unit = {}
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.testTag(stringResource(id = R.string.headerContent_iconButton_testTag)),
            onClick = onClickLogo
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                imageVector = logo,
                contentDescription = stringResource(R.string.headerContent_iconButton_description)
            )
        }
        Text(
            modifier = Modifier
                .weight(1f),
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        menu()
    }
}

@Preview(showBackground = true)
@Composable
fun HeaderContentPreview(){
    HeaderContent(
        modifier = Modifier.fillMaxWidth(),
        logo = Icons.AutoMirrored.Filled.ArrowBack,
        title = "Header",
        onClickLogo = {},
        menu = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Menu"
                    )
                }

                Spacer(Modifier.padding(horizontal = MaterialTheme.dimens.smallSpacing))

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Sharp.Home,
                        contentDescription = "Menu"
                    )
                }
            }
        }
    )
}