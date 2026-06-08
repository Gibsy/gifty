package com.gibs.kadeesebi.presentation.i18n

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class LocaleViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val language = settingsRepository.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AppLanguage.RU)

    val themeMode = settingsRepository.themeMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ThemeMode.SYSTEM)

    val currency = settingsRepository.currency
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), Currency.KZT)

    val onboardingDone = settingsRepository.onboardingDone
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { settingsRepository.setLanguage(language) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { settingsRepository.setThemeMode(mode) }
    }

    fun setCurrency(currency: Currency) {
        Money.currency = currency
        viewModelScope.launch { settingsRepository.setCurrency(currency) }
    }

    fun completeOnboarding() {
        viewModelScope.launch { settingsRepository.setOnboardingDone(true) }
    }
}
