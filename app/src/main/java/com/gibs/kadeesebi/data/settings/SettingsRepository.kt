package com.gibs.kadeesebi.data.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gibs.kadeesebi.domain.model.AppLanguage
import com.gibs.kadeesebi.domain.model.Currency
import com.gibs.kadeesebi.domain.model.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore("kade_settings")

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val currencyKey = stringPreferencesKey("currency")
    private val languageKey = stringPreferencesKey("app_language")
    private val themeKey = stringPreferencesKey("theme_mode")
    private val onboardingKey = booleanPreferencesKey("onboarding_completed")
    private val cloudFolderKey = stringPreferencesKey("cloud_folder_uri")
    private val lastCloudBackupDayKey = stringPreferencesKey("last_cloud_backup_day")

    val currency: Flow<Currency> = context.settingsDataStore.data.map { prefs ->
        Currency.fromCode(prefs[currencyKey])
    }

    suspend fun setCurrency(currency: Currency) {
        context.settingsDataStore.edit { it[currencyKey] = currency.code }
    }

    val language: Flow<AppLanguage> = context.settingsDataStore.data.map { prefs ->
        AppLanguage.fromCode(prefs[languageKey])
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.settingsDataStore.edit { it[languageKey] = language.code }
    }

    val themeMode: Flow<ThemeMode> = context.settingsDataStore.data.map { prefs ->
        ThemeMode.fromCode(prefs[themeKey])
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.settingsDataStore.edit { it[themeKey] = mode.code }
    }

    val onboardingDone: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[onboardingKey] ?: false
    }

    suspend fun setOnboardingDone(done: Boolean) {
        context.settingsDataStore.edit { it[onboardingKey] = done }
    }

    val cloudFolderUri: Flow<String?> = context.settingsDataStore.data.map { prefs ->
        prefs[cloudFolderKey]
    }

    suspend fun setCloudFolderUri(uri: String?) {
        context.settingsDataStore.edit {
            if (uri == null) it.remove(cloudFolderKey) else it[cloudFolderKey] = uri
        }
    }

    val lastCloudBackupDay: Flow<String?> = context.settingsDataStore.data.map { prefs ->
        prefs[lastCloudBackupDayKey]
    }

    suspend fun setLastCloudBackupDay(day: String) {
        context.settingsDataStore.edit { it[lastCloudBackupDayKey] = day }
    }
}
