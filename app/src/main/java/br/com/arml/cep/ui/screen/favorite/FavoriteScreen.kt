package br.com.arml.cep.ui.screen.favorite

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

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()
    var isDetailPaneExpanded by rememberSaveable { mutableStateOf(false) }
    var isExtraPaneExpanded by rememberSaveable { mutableStateOf(false) }

    NavigableListDetailPaneScaffold(
        modifier = modifier,
        navigator = navigator,
        listPane = {
            FavoriteListScreen {
                scope.launch {
                    isDetailPaneExpanded = true
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail
                    )
                }
            }
        },

        detailPane = {
            if (isDetailPaneExpanded) {
                FavoriteEntryScreen(
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
            }
        },

        extraPane = {
            if (isDetailPaneExpanded == false) isExtraPaneExpanded = false
            if (isExtraPaneExpanded) {
                FavoriteExtraScreen {
                    isExtraPaneExpanded = false
                    scope.launch {
                        navigator.navigateBack()
                    }
                }
            }
        }
    )
}

@Composable
fun FavoriteListScreen(
    modifier: Modifier = Modifier,
    onNavigateToList: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiary),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onNavigateToList) { Text("To Favorite Entry") }
    }
}

@Composable
fun FavoriteEntryScreen(
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
            Button(onClick = onNavigateToExtra) { Text("To Favorite Extra") }
            Button(onClick = onNavigateBack) { Text("Back to Favorite List") }
        }
    }
}

@Composable
fun FavoriteExtraScreen(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onNavigateBack) { Text("Back to Favorite Entry") }
    }
}