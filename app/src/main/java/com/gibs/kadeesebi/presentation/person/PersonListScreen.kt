package com.gibs.kadeesebi.presentation.person

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gibs.kadeesebi.presentation.common.EmptyState
import com.gibs.kadeesebi.presentation.common.InitialsAvatar
import com.gibs.kadeesebi.presentation.i18n.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonListScreen(
    onOpenPerson: (String) -> Unit,
    viewModel: PersonListViewModel = hiltViewModel(),
) {
    val strings = LocalStrings.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var circleExpanded by remember { mutableStateOf(false) }
    var selectedCircleId by remember { mutableStateOf<String?>(null) }
    var relation by remember { mutableStateOf("") }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text(strings.navPeople, fontWeight = FontWeight.Bold) }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showDialog = true },
                icon = { Icon(Icons.Filled.PersonAdd, contentDescription = null) },
                text = { Text(strings.addPerson) },
            )
        },
    ) { padding ->
        if (state.rows.isEmpty()) {
            EmptyState(
                icon = Icons.Filled.Group,
                title = strings.emptyPeopleTitle,
                subtitle = strings.emptyPeopleBody,
                modifier = Modifier.padding(padding),
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.rows, key = { it.person.id }) { row ->
                    Card(
                        onClick = { onOpenPerson(row.person.id) },
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            val avatarColor = row.circleColor?.let { Color(it) } ?: MaterialTheme.colorScheme.primary
                            InitialsAvatar(name = row.person.fullName, size = 48.dp, color = avatarColor)
                            Spacer(Modifier.width(14.dp))
                            Column(Modifier.weight(1f)) {
                                Text(
                                    row.person.fullName,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                row.person.relation?.takeIf { it.isNotBlank() }?.let { rel ->
                                    Text(
                                        rel,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                row.circle?.let { circle ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(top = 2.dp),
                                    ) {
                                        Box(
                                            Modifier
                                                .size(10.dp)
                                                .background(avatarColor, CircleShape),
                                        )
                                        Text(
                                            "  ${strings.circleName(circle)}",
                                            style = MaterialTheme.typography.bodyMedium,
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

    if (showDialog) {
        val selectedCircleName = state.circles.firstOrNull { it.id == selectedCircleId }?.let { strings.circleName(it) } ?: strings.noCircle
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(strings.newPerson) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(strings.name) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    OutlinedTextField(
                        value = relation,
                        onValueChange = { relation = it },
                        label = { Text(strings.relationLabel) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    ExposedDropdownMenuBox(expanded = circleExpanded, onExpandedChange = { circleExpanded = it }) {
                        OutlinedTextField(
                            value = selectedCircleName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(strings.circle) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = circleExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        )
                        ExposedDropdownMenu(
                            expanded = circleExpanded,
                            onDismissRequest = { circleExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text(strings.noCircle) },
                                onClick = { selectedCircleId = null; circleExpanded = false },
                            )
                            state.circles.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(strings.circleName(c)) },
                                    onClick = { selectedCircleId = c.id; circleExpanded = false },
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.addPerson(name, selectedCircleId, relation)
                    name = ""
                    selectedCircleId = null
                    relation = ""
                    showDialog = false
                }) { Text(strings.add) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(strings.cancel) }
            },
        )
    }
}
