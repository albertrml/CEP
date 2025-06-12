package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import br.com.arml.cep.ui.theme.dimens

@Composable
fun Header(
    modifier: Modifier = Modifier,
    logo: ImageVector? = null,
    colorLogo: Color? = null,
    title: String,
    onClickLogo: () -> Unit = {}
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.mediumSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            logo?.let { logo ->
                IconButton(
                    modifier = Modifier,
                    onClick = onClickLogo
                ) {
                    colorLogo?.let{ colorLogo ->
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = logo,
                            contentDescription = title,
                            tint = colorLogo
                        )
                    } ?: Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = logo,
                        contentDescription = title
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            thickness = MaterialTheme.dimens.largeThickness,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}