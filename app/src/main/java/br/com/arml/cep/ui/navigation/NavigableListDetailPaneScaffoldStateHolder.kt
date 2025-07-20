package br.com.arml.cep.ui.navigation

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
data class NavigableListDetailPaneScaffoldStateHolder(
    val navigator: ThreePaneScaffoldNavigator<Any>,
    private val scope: CoroutineScope,
) {

    private val isDetailPaneEffectivelyVisible by derivedStateOf {
        navigator.currentDestination?.run {
            pane == ListDetailPaneScaffoldRole.Detail ||
                    pane == ListDetailPaneScaffoldRole.Extra
        } ?: false
    }

    private val isExtraPaneEffectivelyVisible by derivedStateOf {
        navigator.currentDestination?.pane == ListDetailPaneScaffoldRole.Extra
    }

    fun navigateBackToListPane(onEvent: () -> Unit) {
        onEvent()
        scope.launch {
            while (navigator.currentDestination != null){
                navigator.navigateBack()
            }
            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
        }
    }

    fun navigateBackToDetailPane(onEvent: () -> Unit) {
        onEvent()
        scope.launch {
            navigator.navigateBack()
        }
    }

    fun navigateToDetailPane(onEvent: () -> Unit) {
        onEvent()
        scope.launch { navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Detail) }
    }

    fun navigateToExtraPane(
        onEvent: () -> Unit,
    ) {
        onEvent()
        scope.launch { navigator.navigateTo(pane = ListDetailPaneScaffoldRole.Extra) }
    }

    @Composable
    fun ShowDetailPane(content: @Composable () -> Unit) {
        if (isDetailPaneEffectivelyVisible) {
            content()
        }
    }

    @Composable
    fun ShowExtraPane(content: @Composable () -> Unit) {
        if (isExtraPaneEffectivelyVisible) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun rememberNavigableListDetailPaneScaffoldStateHolder(
    navigator: ThreePaneScaffoldNavigator<Any> = rememberListDetailPaneScaffoldNavigator(),
    scope: CoroutineScope = rememberCoroutineScope()
): NavigableListDetailPaneScaffoldStateHolder = remember(navigator, scope) {
    NavigableListDetailPaneScaffoldStateHolder(navigator, scope)
}