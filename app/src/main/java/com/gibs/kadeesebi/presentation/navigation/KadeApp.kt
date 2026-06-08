package com.gibs.kadeesebi.presentation.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gibs.kadeesebi.domain.model.ThemeMode
import com.gibs.kadeesebi.presentation.analytics.AnalyticsScreen
import com.gibs.kadeesebi.presentation.circles.CirclesScreen
import com.gibs.kadeesebi.presentation.eventtypes.EventTypesScreen
import com.gibs.kadeesebi.presentation.i18n.LocalStrings
import com.gibs.kadeesebi.presentation.i18n.LocaleViewModel
import com.gibs.kadeesebi.presentation.i18n.Strings
import com.gibs.kadeesebi.presentation.i18n.stringsFor
import com.gibs.kadeesebi.presentation.onboarding.OnboardingScreen
import com.gibs.kadeesebi.presentation.person.PersonDetailScreen
import com.gibs.kadeesebi.presentation.person.PersonListScreen
import com.gibs.kadeesebi.presentation.settings.SettingsScreen
import com.gibs.kadeesebi.presentation.theme.KadeEsebiTheme
import com.gibs.kadeesebi.presentation.toidetail.ToiDetailScreen
import com.gibs.kadeesebi.presentation.tois.AddEditToiScreen
import com.gibs.kadeesebi.presentation.tois.ToiListScreen

sealed class Screen(val route: String) {
    data object Tois : Screen("tois")
    data object People : Screen("people")
    data object Analytics : Screen("analytics")
    data object Settings : Screen("settings")
    data object EventTypes : Screen("eventtypes")
    data object Circles : Screen("circles")
    data object AddToi : Screen("toi/add")
    data class EditToi(val id: String) : Screen("toi/edit/$id") {
        companion object { const val ROUTE = "toi/edit/{toiId}" }
    }
    data class ToiDetail(val id: String) : Screen("toidetail/$id") {
        companion object { const val ROUTE = "toidetail/{toiId}" }
    }
    data class PersonDetail(val id: String) : Screen("person/$id") {
        companion object { const val ROUTE = "person/{personId}" }
    }
}

private data class TopTab(val screen: Screen, val label: (Strings) -> String, val icon: ImageVector)

@Composable
fun KadeApp(
    localeViewModel: LocaleViewModel = hiltViewModel(),
) {
    val language by localeViewModel.language.collectAsStateWithLifecycle()
    val themeMode by localeViewModel.themeMode.collectAsStateWithLifecycle()
    val currency by localeViewModel.currency.collectAsStateWithLifecycle()

    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val onboardingDone by localeViewModel.onboardingDone.collectAsStateWithLifecycle()

    KadeEsebiTheme(darkTheme = darkTheme) {
        Surface {
            CompositionLocalProvider(LocalStrings provides stringsFor(language)) {
                when (onboardingDone) {
                    false -> OnboardingScreen(
                        language = language,
                        themeMode = themeMode,
                        currency = currency,
                        onLanguageChange = localeViewModel::setLanguage,
                        onThemeChange = localeViewModel::setThemeMode,
                        onCurrencyChange = localeViewModel::setCurrency,
                        onFinish = localeViewModel::completeOnboarding,
                    )
                    true -> KadeAppContent()
                    null -> Unit
                }
            }
        }
    }
}

@Composable
private fun KadeAppContent() {
    val strings = LocalStrings.current
    val navController = rememberNavController()
    val tabs = listOf(
        TopTab(Screen.Tois, { it.navTois }, Icons.Filled.Celebration),
        TopTab(Screen.People, { it.navPeople }, Icons.Filled.People),
        TopTab(Screen.Analytics, { it.navAnalytics }, Icons.Filled.BarChart),
        TopTab(Screen.Settings, { it.navSettings }, Icons.Filled.Settings),
    )

    Scaffold(
        bottomBar = {
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = backStackEntry?.destination
            NavigationBar {
                tabs.forEach { tab ->
                    val label = tab.label(strings)
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.screen.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = label) },
                        label = { Text(label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Tois.route,
            modifier = androidx.compose.ui.Modifier.padding(innerPadding),
        ) {
            composable(Screen.Tois.route) {
                ToiListScreen(
                    onAddToi = { navController.navigate(Screen.AddToi.route) },
                    onOpenToi = { id -> navController.navigate("toidetail/$id") },
                )
            }
            composable(Screen.People.route) {
                PersonListScreen(
                    onOpenPerson = { id -> navController.navigate("person/$id") },
                )
            }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onManageEventTypes = { navController.navigate(Screen.EventTypes.route) },
                    onManageCircles = { navController.navigate(Screen.Circles.route) },
                )
            }
            composable(Screen.EventTypes.route) {
                EventTypesScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.Circles.route) {
                CirclesScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.AddToi.route) {
                AddEditToiScreen(onDone = { navController.popBackStack() })
            }
            composable(Screen.EditToi.ROUTE) {
                AddEditToiScreen(onDone = { navController.popBackStack() })
            }
            composable(Screen.ToiDetail.ROUTE) {
                ToiDetailScreen(
                    onBack = { navController.popBackStack() },
                    onEdit = { id -> navController.navigate("toi/edit/$id") },
                )
            }
            composable(Screen.PersonDetail.ROUTE) {
                PersonDetailScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
