package br.com.arml.cep.ui.screen.component.common.fastscroll

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.arml.cep.R
import br.com.arml.cep.ui.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun FastScroll(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 64.dp,
    content: @Composable (ScrollState) -> Unit
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val canScrollForward by remember { derivedStateOf { scrollState.value < scrollState.maxValue } }
    val showFab by remember { derivedStateOf { scrollState.maxValue > 0 } }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        content(scrollState)
        if (showFab) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(buttonSize)
                    .padding(
                        bottom = MaterialTheme.dimens.mediumMargin,
                        end = MaterialTheme.dimens.mediumMargin
                    )
                    .testTag(stringResource(R.string.fastScroll_fab_testTag))
                ,
                onClick = {
                    coroutineScope.launch {
                        if (canScrollForward) {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        } else {
                            scrollState.animateScrollTo(0)
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = if (canScrollForward)
                        Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = if (canScrollForward)
                        stringResource(R.string.fastScroll_fabToEnd_contentDescription)
                    else
                        stringResource(R.string.fastScroll_fabToStart_contentDescription)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FastScrollPreview(){
    FastScroll(
        modifier = Modifier.fillMaxSize()
    ){ scrollState ->
        Column(modifier = Modifier.verticalScroll(scrollState) ){
            repeat(10){ Text(text = "Item $it") }
        }
    }
}