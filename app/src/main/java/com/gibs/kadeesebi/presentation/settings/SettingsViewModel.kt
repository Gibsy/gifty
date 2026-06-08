package com.gibs.kadeesebi.presentation.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.data.backup.BackupManager
import com.gibs.kadeesebi.data.export.ExportManager
import com.gibs.kadeesebi.data.settings.SettingsRepository
import com.gibs.kadeesebi.domain.model.AppLanguage
import com.gibs.kadeesebi.domain.model.Currency
import com.gibs.kadeesebi.domain.model.ThemeMode
import com.gibs.kadeesebi.domain.util.Money
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val exportManager: ExportManager,
    private val backupManager: BackupManager,
) : ViewModel() {

    val currency = settingsRepository.currency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Currency.KZT)

    val language = settingsRepository.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppLanguage.RU)

    val themeMode = settingsRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.SYSTEM)

    fun setCurrency(currency: Currency) {
        Money.currency = currency
        viewModelScope.launch { settingsRepository.setCurrency(currency) }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { settingsRepository.setLanguage(language) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun exportCsv(onReady: (Uri) -> Unit) {
        viewModelScope.launch { onReady(exportManager.exportCsv()) }
    }

    fun exportPdf(onReady: (Uri) -> Unit) {
        viewModelScope.launch { onReady(exportManager.exportPdf()) }
    }

    fun backup(uri: Uri, onDone: (Boolean) -> Unit) {
        viewModelScope.launch { onDone(runCatching { backupManager.export(uri) }.isSuccess) }
    }

    fun restore(uri: Uri, onDone: (Boolean) -> Unit) {
        viewModelScope.launch { onDone(runCatching { backupManager.import(uri) }.isSuccess) }
    }
}
