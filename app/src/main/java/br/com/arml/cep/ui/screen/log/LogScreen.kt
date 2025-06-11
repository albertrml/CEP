package br.com.arml.cep.ui.screen.log

import android.content.ClipData
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import br.com.arml.cep.R
import br.com.arml.cep.ui.screen.component.log.LogListComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun LogScreen(modifier: Modifier = Modifier) {
    val clipboardManager = LocalClipboard.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LogListComponent(
            modifier = Modifier.padding(innerPadding),
            onCopyToClipboard = { entry ->
                scope.launch {
                    val clippedCep: String = entry.cep.text
                    val clipData = ClipData.newPlainText("cep", clippedCep)
                    clipboardManager.setClipEntry(clipData.toClipEntry())

                    snackbarHostState.showSnackbar(
                        message = context.getString(
                            R.string.log_list_clipboard_msg,
                            entry.cep.toFormattedCep()
                        ),
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}