package com.gibs.kadeesebi.presentation.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gibs.kadeesebi.domain.model.AppLanguage
import com.gibs.kadeesebi.domain.model.Currency
import com.gibs.kadeesebi.domain.model.ThemeMode
import com.gibs.kadeesebi.presentation.i18n.LocalStrings

@Composable
fun OnboardingScreen(
    language: AppLanguage,
    themeMode: ThemeMode,
    currency: Currency,
    onLanguageChange: (AppLanguage) -> Unit,
    onThemeChange: (ThemeMode) -> Unit,
    onCurrencyChange: (Currency) -> Unit,
    onFinish: () -> Unit,
) {
    val strings = LocalStrings.current
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "\uD83C\uDF81 Gifty",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Text(
                text = strings.onboardingWelcomeBody,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Text(strings.languageTitle, style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = language == AppLanguage.RU,
                    onClick = { onLanguageChange(AppLanguage.RU) },
                    label = { Text(strings.languageRussian) },
                )
                FilterChip(
                    selected = language == AppLanguage.KK,
                    onClick = { onLanguageChange(AppLanguage.KK) },
                    label = { Text(strings.languageKazakh) },
                )
                FilterChip(
                    selected = language == AppLanguage.EN,
                    onClick = { onLanguageChange(AppLanguage.EN) },
                    label = { Text(strings.languageEnglish) },
                )
            }

            Text(strings.themeTitle, style = MaterialTheme.typography.titleMedium)
            Text(strings.onboardingThemeHint, style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = themeMode == ThemeMode.SYSTEM,
                    onClick = { onThemeChange(ThemeMode.SYSTEM) },
                    label = { Text(strings.themeSystem) },
                )
                FilterChip(
                    selected = themeMode == ThemeMode.LIGHT,
                    onClick = { onThemeChange(ThemeMode.LIGHT) },
                    label = { Text(strings.themeLight) },
                )
                FilterChip(
                    selected = themeMode == ThemeMode.DARK,
                    onClick = { onThemeChange(ThemeMode.DARK) },
                    label = { Text(strings.themeDark) },
                )
            }

            Text(strings.currencyTitle, style = MaterialTheme.typography.titleMedium)
            Text(strings.onboardingCurrencyHint, style = MaterialTheme.typography.bodySmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = currency == Currency.RUB,
                    onClick = { onCurrencyChange(Currency.RUB) },
                    label = { Text(strings.currencyRuble) },
                )
                FilterChip(
                    selected = currency == Currency.KZT,
                    onClick = { onCurrencyChange(Currency.KZT) },
                    label = { Text(strings.currencyTenge) },
                )
                FilterChip(
                    selected = currency == Currency.USD,
                    onClick = { onCurrencyChange(Currency.USD) },
                    label = { Text(strings.currencyDollar) },
                )
            }

            Card(Modifier.fillMaxWidth()) {
                Column(
                    Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(strings.onboardingEventTypesTitle, style = MaterialTheme.typography.titleSmall)
                    Text(strings.onboardingEventTypesHint, style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(Modifier.height(4.dp))
            Button(
                onClick = onFinish,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(strings.onboardingStart)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
