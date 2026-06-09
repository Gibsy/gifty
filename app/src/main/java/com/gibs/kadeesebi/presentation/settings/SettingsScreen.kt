package com.gibs.kadeesebi.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gibs.kadeesebi.BuildConfig
import com.gibs.kadeesebi.domain.model.AppLanguage
import com.gibs.kadeesebi.domain.model.Currency
import com.gibs.kadeesebi.domain.model.ThemeMode
import com.gibs.kadeesebi.presentation.i18n.LocalStrings
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onManageEventTypes: () -> Unit,
    onManageCircles: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val strings = LocalStrings.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbar = remember { SnackbarHostState() }
    val currency by viewModel.currency.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
    val backupFileName = remember {
        "gifty_backup_" + SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) + ".json"
    }
    val cloudFolder by viewModel.cloudFolderUri.collectAsStateWithLifecycle()

    fun share(uri: Uri, mime: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, strings.shareTitle))
    }

    val backupLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json"),
    ) { uri: Uri? ->
        if (uri != null) viewModel.backup(uri) { ok ->
            scope.launch { snackbar.showSnackbar(if (ok) strings.backupSaved else strings.backupSaveFailed) }
        }
    }
    val restoreLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri != null) viewModel.restore(uri) { ok ->
            scope.launch { snackbar.showSnackbar(if (ok) strings.dataRestored else strings.restoreFailed) }
        }
    }
    val cloudFolderLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocumentTree(),
    ) { uri: Uri? ->
        if (uri != null) {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )
            viewModel.connectCloudFolder(uri) { ok ->
                scope.launch { snackbar.showSnackbar(if (ok) strings.cloudSaved else strings.cloudSaveFailed) }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(strings.navSettings) }) },
        snackbarHost = { SnackbarHost(snackbar) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.languageTitle, style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = language == AppLanguage.RU,
                            onClick = { viewModel.setLanguage(AppLanguage.RU) },
                            label = { Text(strings.languageRussian) },
                        )
                        FilterChip(
                            selected = language == AppLanguage.KK,
                            onClick = { viewModel.setLanguage(AppLanguage.KK) },
                            label = { Text(strings.languageKazakh) },
                        )
                        FilterChip(
                            selected = language == AppLanguage.EN,
                            onClick = { viewModel.setLanguage(AppLanguage.EN) },
                            label = { Text(strings.languageEnglish) },
                        )
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.themeTitle, style = MaterialTheme.typography.titleMedium)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = themeMode == ThemeMode.SYSTEM,
                            onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                            label = { Text(strings.themeSystem) },
                        )
                        FilterChip(
                            selected = themeMode == ThemeMode.LIGHT,
                            onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                            label = { Text(strings.themeLight) },
                        )
                        FilterChip(
                            selected = themeMode == ThemeMode.DARK,
                            onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                            label = { Text(strings.themeDark) },
                        )
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.circlesTitle, style = MaterialTheme.typography.titleMedium)
                    Text(strings.circlesBody, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onManageCircles,
                    ) {
                        Text(strings.manageCircles)
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.eventTypesTitle, style = MaterialTheme.typography.titleMedium)
                    Text(strings.eventTypesBody, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onManageEventTypes,
                    ) {
                        Text(strings.manageEventTypes)
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.currencyTitle, style = MaterialTheme.typography.titleMedium)
                    Text(strings.currencyBody, style = MaterialTheme.typography.bodySmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = currency == Currency.RUB,
                            onClick = { viewModel.setCurrency(Currency.RUB) },
                            label = { Text(strings.currencyRuble) },
                        )
                        FilterChip(
                            selected = currency == Currency.KZT,
                            onClick = { viewModel.setCurrency(Currency.KZT) },
                            label = { Text(strings.currencyTenge) },
                        )
                        FilterChip(
                            selected = currency == Currency.USD,
                            onClick = { viewModel.setCurrency(Currency.USD) },
                            label = { Text(strings.currencyDollar) },
                        )
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.exportTitle, style = MaterialTheme.typography.titleMedium)
                    Text(strings.exportBody, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.exportCsv { uri -> share(uri, "text/csv") } },
                    ) { Text(strings.exportCsv) }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.exportPdf { uri -> share(uri, "application/pdf") } },
                    ) { Text(strings.exportPdf) }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.backupTitle, style = MaterialTheme.typography.titleMedium)
                    Text(strings.backupBody, style = MaterialTheme.typography.bodySmall)
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { backupLauncher.launch(backupFileName) },
                    ) { Text(strings.createBackup) }
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { restoreLauncher.launch(arrayOf("application/json", "application/octet-stream", "text/plain", "*/*")) },
                    ) { Text(strings.restoreBackup) }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(strings.cloudTitle, style = MaterialTheme.typography.titleMedium)
                    Text(strings.cloudBody, style = MaterialTheme.typography.bodySmall)
                    if (cloudFolder != null) {
                        Text(
                            strings.cloudConnected,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.backupToCloud { ok ->
                                    scope.launch { snackbar.showSnackbar(if (ok) strings.cloudSaved else strings.cloudSaveFailed) }
                                }
                            },
                        ) { Text(strings.cloudSaveNow) }
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { viewModel.disconnectCloudFolder() },
                        ) { Text(strings.cloudDisconnect) }
                    } else {
                        OutlinedButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { cloudFolderLauncher.launch(null) },
                        ) { Text(strings.cloudConnect) }
                    }
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(strings.aboutTitle, style = MaterialTheme.typography.titleMedium)
                    Text("Gifty", style = MaterialTheme.typography.bodyLarge)
                    Text("${strings.aboutDeveloperLabel}: gibsy")
                    Text("${strings.aboutVersionLabel}: ${BuildConfig.VERSION_NAME}")
                }
            }
        }
    }
}
