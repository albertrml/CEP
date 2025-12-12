package br.com.arml.cep.ui.screen.component.common.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.MAX_TITLE_LENGTH
import br.com.arml.cep.model.domain.MIN_TITLE_LENGTH
import br.com.arml.cep.ui.screen.component.common.field.AppTextField

@Composable
fun TitleFilter(
    modifier: Modifier = Modifier,
    maxSize: Int,
    onFilterByTitle: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val isButtonActive by remember {
        derivedStateOf {
            text.length in MIN_TITLE_LENGTH..MAX_TITLE_LENGTH
        }
    }

    Column(
        modifier = modifier
            .testTag(stringResource(R.string.titleFilter_component_testTag)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppTextField(
            modifier = Modifier
                .testTag(stringResource(R.string.titleFilter_titleField_testTag)),
            nameField = stringResource(R.string.titleFilter_titleField_label),
            text = text,
            onChangeText = { newText -> text = newText },
            maxSize = maxSize
        )
        Button(
            modifier = Modifier
                .testTag(stringResource(R.string.titleFilter_filterButton_testTag)),
            enabled = isButtonActive,
            onClick = { onFilterByTitle(text) },
        ) {
            Text(text = stringResource(R.string.titleFilter_filterButton_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FieldFilterPreview() {
    TitleFilter(
        maxSize = 100,
        onFilterByTitle = {}
    )
}