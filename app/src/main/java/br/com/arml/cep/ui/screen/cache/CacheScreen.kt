@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package br.com.arml.cep.ui.screen.cache

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@Composable
fun CacheScreen(
    modifier: Modifier = Modifier,
){
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }
    var isExtraPaneExpanded by rememberSaveable { mutableStateOf(false) }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            CacheListScreen {
                scope.launch {
                    isDetailPaneExpanded = true
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail
                    )
                }
            }
        },
        detailPane = {
            CacheEntryScreen(
                onNavigateToExtra = {
                    isExtraPaneExpanded = true
                    scope.launch {
                        navigator.navigateTo(
                            pane = ListDetailPaneScaffoldRole.Extra
                        )
                    }
                },

                onNavigateBack = {
                    scope.launch {
                        isDetailPaneExpanded = false
                        navigator.navigateBack()
                    }
                }
            )
        },
        extraPane = {
            CacheExtraScreen {
                isExtraPaneExpanded = false
                scope.launch {
                    navigator.navigateBack()
                }
            }
        }
    )

}


@Composable
fun CacheListScreen(
    modifier: Modifier = Modifier,
    onNavigateToList: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onNavigateToList) { Text("To Cache Entry") }
    }
}

@Composable
fun CacheEntryScreen(
    modifier: Modifier = Modifier,
    onNavigateToExtra: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(onClick = onNavigateToExtra) { Text("To Cache Extra") }
            Button(onClick = onNavigateBack) { Text("Back to Cache List") }
        }
    }
}

@Composable
fun CacheExtraScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onNavigateBack) { Text("Back to Cache Entry") }
    }
}