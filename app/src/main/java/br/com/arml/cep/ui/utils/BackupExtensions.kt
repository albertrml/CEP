package br.com.arml.cep.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import br.com.arml.cep.model.exception.BackupException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun exportBackupLauncher(
    context: Context,
    json: String
): ManagedActivityResultLauncher<Intent, ActivityResult> = rememberLauncherForActivityResult(
    ActivityResultContracts.StartActivityForResult()
) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
        result.data?.data?.let { uri -> exportToUri(context, uri, json) }
    }
}

private fun exportToUri(context: Context, uri: Uri, json: String) {
    context.contentResolver.openOutputStream(uri)?.use {
        it.write(json.toByteArray())
    } ?: throw BackupException.ExportUriException()
}

fun getExportIntent(): Intent {
    val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    val fileName = "CEPBKP_${formatter.format(Date())}.json"
    val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
        addCategory(Intent.CATEGORY_OPENABLE)
        type = "application/json"
        putExtra(Intent.EXTRA_TITLE, fileName)
    }
    return intent
}

fun getImportIntent(): Intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
    addCategory(Intent.CATEGORY_OPENABLE)
    type = "application/json"
}

@Composable
fun importBackupLauncher(
    context: Context,
    onSuccess: (json: String) -> Unit
): ManagedActivityResultLauncher<Intent, ActivityResult> {
    val currentOnSuccess by rememberUpdatedState(onSuccess)
    val currentContext by rememberUpdatedState(context)

    return rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                val json = importFromUri(currentContext, uri)
                currentOnSuccess(json)
            }
        }
    }
}

fun importFromUri(context: Context, uri: Uri): String {
    return context.contentResolver.openInputStream(uri)?.use {
        it.bufferedReader().readText()
    } ?: throw BackupException.ImportUriException()
}
