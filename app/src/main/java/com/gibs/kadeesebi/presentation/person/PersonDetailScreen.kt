package com.gibs.kadeesebi.presentation.person

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gibs.kadeesebi.domain.model.GiftDirection
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.util.Money
import com.gibs.kadeesebi.presentation.common.InitialsAvatar
import com.gibs.kadeesebi.presentation.common.ReceiptPhotoPicker
import com.gibs.kadeesebi.presentation.common.ReceiptThumbnail
import com.gibs.kadeesebi.presentation.common.SectionHeader
import com.gibs.kadeesebi.presentation.common.StatusPill
import com.gibs.kadeesebi.presentation.i18n.LocalStrings
import com.gibs.kadeesebi.presentation.i18n.Strings
import java.text.SimpleDateFormat
import java.util.Date

private val PositiveGreen = Color(0xFF2E8B57)
private val NegativeRed = Color(0xFFC0392B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailScreen(
    onBack: () -> Unit,
    viewModel: PersonDetailViewModel = hiltViewModel(),
) {
    val strings = LocalStrings.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val tois by viewModel.tois.collectAsStateWithLifecycle()
    val eventTypes by viewModel.eventTypes.collectAsStateWithLifecycle()
    val dateFmt = remember(strings.locale) { SimpleDateFormat("d MMM yyyy", strings.locale) }
    var showAddGift by remember { mutableStateOf(false) }
    var showDeletePerson by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.person?.fullName ?: strings.cardFallback, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    IconButton(onClick = { showDeletePerson = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = strings.deletePerson)
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddGift = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(strings.addGift) },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            state.balance?.let { b ->
                item {
                    Card(
                        Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                state.person?.let { InitialsAvatar(name = it.fullName, size = 48.dp) }
                                Spacer(Modifier.width(14.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(strings.balanceTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    val netStatus = when {
                                        b.net > 0 -> strings.netYouAhead
                                        b.net < 0 -> strings.netTheyAhead
                                        else -> strings.netInBalance
                                    }
                                    val statusColor = when {
                                        b.net > 0 -> PositiveGreen
                                        b.net < 0 -> NegativeRed
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                    Text(netStatus, style = MaterialTheme.typography.bodyMedium, color = statusColor, fontWeight = FontWeight.SemiBold)
                                }
                            }
                            Spacer(Modifier.size(14.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                BalanceCell(
                                    modifier = Modifier.weight(1f),
                                    label = strings.gaveToYou,
                                    value = Money.format(b.received),
                                    accent = PositiveGreen,
                                )
                                BalanceCell(
                                    modifier = Modifier.weight(1f),
                                    label = strings.youGavePerson,
                                    value = Money.format(b.given),
                                    accent = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Spacer(Modifier.size(12.dp))
                            HorizontalDivider()
                            Spacer(Modifier.size(12.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(strings.balance, fontWeight = FontWeight.Bold)
                                val netColor = if (b.net >= 0) PositiveGreen else NegativeRed
                                Text(Money.format(b.net), color = netColor, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            }
                            Spacer(Modifier.size(10.dp))
                            StatusPill(
                                text = strings.suggestedFuture(Money.format(b.suggested)),
                                container = MaterialTheme.colorScheme.secondaryContainer,
                                content = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                        }
                    }
                }
            }

            item { SectionHeader(strings.giftHistory) }

            if (state.gifts.isEmpty()) {
                item {
                    Text(
                        strings.emptyGiftsPerson,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
            items(state.gifts, key = { it.id }) { gift ->
                val received = gift.direction == GiftDirection.RECEIVED
                val label = if (received) strings.gaveToYou else strings.youGavePerson
                Card(
                    Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        gift.photoUri?.let {
                            ReceiptThumbnail(photoUri = it, size = 48.dp)
                            Spacer(Modifier.width(12.dp))
                        } ?: run {
                            Icon(
                                if (received) Icons.Filled.SouthWest else Icons.Filled.NorthEast,
                                contentDescription = null,
                                tint = if (received) PositiveGreen else NegativeRed,
                                modifier = Modifier.size(24.dp),
                            )
                            Spacer(Modifier.width(12.dp))
                        }
                        Column(Modifier.weight(1f)) {
                            Text(label, fontWeight = FontWeight.SemiBold)
                            Text(
                                dateFmt.format(Date(gift.date)),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        Text(Money.format(gift.amount), fontWeight = FontWeight.Bold)
                        IconButton(onClick = { viewModel.deleteGift(gift.id) }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = strings.delete,
                                tint = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeletePerson) {
        AlertDialog(
            onDismissRequest = { showDeletePerson = false },
            title = { Text(strings.deletePerson) },
            text = { Text(strings.deletePersonConfirm) },
            confirmButton = {
                TextButton(onClick = {
                    showDeletePerson = false
                    viewModel.deletePerson(onDeleted = onBack)
                }) { Text(strings.delete) }
            },
            dismissButton = {
                TextButton(onClick = { showDeletePerson = false }) { Text(strings.cancel) }
            },
        )
    }

    if (showAddGift) {
        AddGiftDialog(
            eventTypes = eventTypes,
            strings = strings,
            tois = tois,
            onDismiss = { showAddGift = false },
            onConfirm = { toiId, direction, amount, photoUri ->
                viewModel.addGift(toiId, direction, amount, photoUri)
                showAddGift = false
            },
        )
    }
}

@Composable
private fun BalanceCell(label: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.size(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = accent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddGiftDialog(
    strings: Strings,
    tois: List<Toi>,
    eventTypes: Map<String, com.gibs.kadeesebi.domain.model.EventType>,
    onDismiss: () -> Unit,
    onConfirm: (toiId: String, direction: GiftDirection, amountTenge: Long, photoUri: String?) -> Unit,
) {
    val dateFmt = remember(strings.locale) { SimpleDateFormat("d MMM yyyy", strings.locale) }
    var toiExpanded by remember { mutableStateOf(false) }
    var selectedToiId by remember { mutableStateOf(tois.firstOrNull()?.id) }
    var direction by remember { mutableStateOf(GiftDirection.RECEIVED) }
    var amountText by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }

    val selectedToi = tois.firstOrNull { it.id == selectedToiId }
    val toiLabel = selectedToi?.let { "${it.title?.takeIf { t -> t.isNotBlank() } ?: eventTypes[it.eventTypeId]?.let { e -> strings.eventTypeName(e) } ?: strings.toiFallback} \u00b7 ${dateFmt.format(Date(it.date))}" } ?: ""
    val amount = amountText.filter { it.isDigit() }.toLongOrNull()
    val canSave = selectedToiId != null && amount != null && amount > 0

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.newGift) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (tois.isEmpty()) {
                    Text(strings.addToiFirst)
                } else {
                    ExposedDropdownMenuBox(expanded = toiExpanded, onExpandedChange = { toiExpanded = it }) {
                        OutlinedTextField(
                            value = toiLabel,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(strings.toi) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = toiExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        )
                        ExposedDropdownMenu(
                            expanded = toiExpanded,
                            onDismissRequest = { toiExpanded = false },
                        ) {
                            tois.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text("${t.title?.takeIf { it.isNotBlank() } ?: eventTypes[t.eventTypeId]?.let { e -> strings.eventTypeName(e) } ?: strings.toiFallback} \u00b7 ${dateFmt.format(Date(t.date))}") },
                                    onClick = { selectedToiId = t.id; toiExpanded = false },
                                )
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = direction == GiftDirection.RECEIVED,
                        onClick = { direction = GiftDirection.RECEIVED },
                        label = { Text(strings.gaveToYou) },
                    )
                    FilterChip(
                        selected = direction == GiftDirection.GIVEN,
                        onClick = { direction = GiftDirection.GIVEN },
                        label = { Text(strings.youGavePerson) },
                    )
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("${strings.amount}, ${Money.currency.symbol}") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )

                ReceiptPhotoPicker(photoUri = photoUri, onPhotoChanged = { photoUri = it })
            }
        },
        confirmButton = {
            TextButton(
                enabled = canSave,
                onClick = { onConfirm(selectedToiId!!, direction, amount!!, photoUri) },
            ) { Text(strings.add) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(strings.cancel) } },
    )
}
