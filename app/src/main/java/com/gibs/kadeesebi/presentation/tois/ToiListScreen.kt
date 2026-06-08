package com.gibs.kadeesebi.presentation.tois

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gibs.kadeesebi.presentation.common.EmptyState
import com.gibs.kadeesebi.presentation.common.EventIcons
import com.gibs.kadeesebi.presentation.common.IconAvatar
import com.gibs.kadeesebi.presentation.common.StatusPill
import com.gibs.kadeesebi.presentation.common.ornamentBackground
import com.gibs.kadeesebi.presentation.i18n.LocalStrings
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToiListScreen(
    onAddToi: () -> Unit,
    onOpenToi: (String) -> Unit,
    viewModel: ToiListViewModel = hiltViewModel(),
) {
    val strings = LocalStrings.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dateFmt = remember(strings.locale) { SimpleDateFormat("d MMM yyyy", strings.locale) }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(strings.navTois, fontWeight = FontWeight.Bold) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddToi,
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(strings.addToi) },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .ornamentBackground(
                    tint = MaterialTheme.colorScheme.primary,
                    base = MaterialTheme.colorScheme.background,
                ),
        ) {
            if (state.items.isEmpty()) {
                EmptyState(
                    icon = Icons.Filled.Celebration,
                    title = strings.emptyToisTitle,
                    subtitle = strings.emptyToisBody,
                    modifier = Modifier.padding(padding),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(state.items, key = { it.toi.id }) { item ->
                        val typeName = item.eventType?.let { strings.eventTypeName(it) } ?: strings.toiFallback
                        Card(
                            onClick = { onOpenToi(item.toi.id) },
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconAvatar(icon = EventIcons.icon(item.eventType?.iconKey), size = 48.dp)
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            item.toi.title?.takeIf { it.isNotBlank() } ?: typeName,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.weight(1f, fill = false),
                                        )
                                        if (item.toi.isOwnToi) {
                                            Spacer(Modifier.width(8.dp))
                                            StatusPill(
                                                text = strings.ownToiSuffix,
                                                container = MaterialTheme.colorScheme.secondaryContainer,
                                                content = MaterialTheme.colorScheme.onSecondaryContainer,
                                            )
                                        }
                                    }
                                    Text(
                                        buildString {
                                            append(dateFmt.format(Date(item.toi.date)))
                                            item.hostName?.let { append("  \u00b7  "); append(it) }
                                        },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    item.toi.place?.takeIf { it.isNotBlank() }?.let { place ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 2.dp),
                                        ) {
                                            Icon(
                                                Icons.Filled.Place,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.size(14.dp),
                                            )
                                            Text(
                                                "  $place",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }
                                }
                                Icon(
                                    Icons.Filled.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
