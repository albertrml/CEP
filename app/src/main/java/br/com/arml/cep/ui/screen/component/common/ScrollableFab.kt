package br.com.arml.cep.ui.screen.component.common

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ScrollableFab(
    modifier: Modifier = Modifier,
    buttonSize: Dp = 64.dp,
    content: @Composable (ScrollState) -> Unit
){
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val canScrollForward by remember {
        derivedStateOf { scrollState.value < scrollState.maxValue }
    }
    val showFab by remember {
        derivedStateOf { scrollState.maxValue > 0 }
    }

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
                    .padding(bottom = 16.dp, end = 16.dp),
                onClick = {
                    coroutineScope.launch {
                        if (canScrollForward) {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        } else {
                            scrollState.animateScrollTo(0)
                        }
                    }
                }
            ){
                Icon(
                    imageVector = if (canScrollForward) Icons.Filled.KeyboardArrowDown
                        else Icons.Filled.KeyboardArrowUp,
                    contentDescription = null
                )
            }
        }
    }
}