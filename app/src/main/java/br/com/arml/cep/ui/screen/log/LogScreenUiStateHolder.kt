package br.com.arml.cep.ui.screen.log

import android.content.ClipData
import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import br.com.arml.cep.R
import br.com.arml.cep.model.domain.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class LogScreenUiStateHolder(
    val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
    private val clipboardManager: Clipboard,
    private val context: Context
){
    fun onCopyToClipboard(entry: Log){
        scope.launch {
            val clippedCep: String = entry.cep.text
            val clipData = ClipData.newPlainText("cep",clippedCep)
            clipboardManager.setClipEntry(clipData.toClipEntry())

            snackbarHostState.showSnackbar(
                message = context.getString(
                    R.string.logScreenUiStateHolder_clipboard_msg,
                    clippedCep
                ),
                duration = SnackbarDuration.Short
            )
        }
    }
}

@Composable
fun rememberLogScreenUiStateHolder(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scope: CoroutineScope = rememberCoroutineScope(),
    clipboardManager: Clipboard = LocalClipboard.current,
    context: Context = LocalContext.current
): LogScreenUiStateHolder = remember(snackbarHostState, scope, clipboardManager, context){
    LogScreenUiStateHolder(snackbarHostState, scope, clipboardManager, context)
}