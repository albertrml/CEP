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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import br.com.arml.cep.ui.theme.dimens

@Composable
fun Header(
    modifier: Modifier = Modifier,
    backImgVec: ImageVector?,
    title: String,
    onBackClick: () -> Unit = {}
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

            backImgVec?.let {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = it,
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