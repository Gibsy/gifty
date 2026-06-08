package com.gibs.kadeesebi.presentation.toidetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
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
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.util.Money
import com.gibs.kadeesebi.presentation.common.EventIcons
import com.gibs.kadeesebi.presentation.common.IconAvatar
import com.gibs.kadeesebi.presentation.common.ReceiptPhotoPicker
import com.gibs.kadeesebi.presentation.common.ReceiptThumbnail
import com.gibs.kadeesebi.presentation.common.SectionHeader
import com.gibs.kadeesebi.presentation.i18n.LocalStrings
import com.gibs.kadeesebi.presentation.i18n.Strings
import java.text.SimpleDateFormat
import java.util.Date

private val PositiveGreen = Color(0xFF2E8B57)
private val NegativeRed = Color(0xFFC0392B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToiDetailScreen(
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    viewModel: ToiDetailViewModel = hiltViewModel(),
) {
    val strings = LocalStrings.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dateFmt = remember(strings.locale) { SimpleDateFormat("d MMMM yyyy", strings.locale) }
    var showAddGift by remember { mutableStateOf(false) }
    val toi = state.toi
    val typeName = state.eventType?.let { strings.eventTypeName(it) } ?: strings.toiFallback

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(toi?.let { it.title?.takeIf { t -> t.isNotBlank() } ?: typeName } ?: strings.toiFallback, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                },
                actions = {
                    if (toi != null) {
                        IconButton(onClick = { onEdit(toi.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = strings.edit)
                        }
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            if (toi != null) {
                item {
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    ) {
                        Column(Modifier.padding(20.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconAvatar(
                                    icon = EventIcons.icon(state.eventType?.iconKey),
                                    size = 52.dp,
                                    container = MaterialTheme.colorScheme.primary,
                                    content = MaterialTheme.colorScheme.onPrimary,
                                )
                                Spacer(Modifier.width(14.dp))
                                Column {
                                    Text(
                                        toi.title?.takeIf { it.isNotBlank() } ?: typeName,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    if (!toi.title.isNullOrBlank()) {
                                        Text(
                                            typeName,
                                            style = MaterialTheme.typography.bodyMedium,
                                        )
                                    }
                                    DetailRow(Icons.Filled.CalendarMonth, dateFmt.format(Date(toi.date)))
                                }
                            }
                            state.hostName?.let {
                                Spacer(Modifier.size(8.dp))
                                DetailRow(Icons.Filled.Person, "${strings.hostLabel}: $it")
                            }
                            toi.place?.takeIf { it.isNotBlank() }?.let {
                                DetailRow(Icons.Filled.Place, it)
                            }
                            Spacer(Modifier.size(14.dp))
                            val label = if (toi.isOwnToi) strings.korimdikCollected(Money.format(state.totalReceived))
                            else strings.youGaveTotal(Money.format(state.totalGiven))
                            Text(
                                label,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            item { SectionHeader(strings.giftsTitle) }

            if (state.gifts.isEmpty()) {
                item {
                    Text(
                        strings.emptyGiftsToi,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }
            items(state.gifts, key = { it.gift.id }) { row ->
                val received = row.gift.direction == GiftDirection.RECEIVED
                val label = if (received) strings.giftReceivedShort else strings.giftGivenShort
                Card(
                    Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(start = 12.dp, end = 4.dp, top = 8.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        row.gift.photoUri?.let {
                            ReceiptThumbnail(photoUri = it, size = 48.dp)
                            Spacer(Modifier.width(12.dp))
                        }
                        Column(Modifier.weight(1f)) {
                            Text(row.personName, fontWeight = FontWeight.Bold)
                            Text(
                                label,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (received) PositiveGreen else NegativeRed,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                        Text(Money.format(row.gift.amount), fontWeight = FontWeight.Bold)
                        IconButton(onClick = { viewModel.deleteGift(row.gift.id) }) {
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

    if (showAddGift) {
        AddGiftForToiDialog(
            strings = strings,
            people = state.people,
            defaultDirection = if (toi?.isOwnToi == true) GiftDirection.RECEIVED else GiftDirection.GIVEN,
            onDismiss = { showAddGift = false },
            onConfirm = { personId, direction, amount, photoUri ->
                viewModel.addGift(personId, direction, amount, photoUri)
                showAddGift = false
            },
        )
    }
}

@Composable
private fun DetailRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 2.dp),
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(6.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddGiftForToiDialog(
    strings: Strings,
    people: List<Person>,
    defaultDirection: GiftDirection,
    onDismiss: () -> Unit,
    onConfirm: (personId: String, direction: GiftDirection, amountTenge: Long, photoUri: String?) -> Unit,
) {
    var personId by remember { mutableStateOf(people.firstOrNull()?.id) }
    var direction by remember { mutableStateOf(defaultDirection) }
    var amountText by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<String?>(null) }
    var personExpanded by remember { mutableStateOf(false) }
    val personName = people.firstOrNull { it.id == personId }?.fullName ?: ""

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(strings.newGift) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (people.isEmpty()) {
                    Text(strings.addPeopleFirstDot)
                } else {
                    ExposedDropdownMenuBox(
                        expanded = personExpanded,
                        onExpandedChange = { personExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = personName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(strings.person) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = personExpanded) },
                            modifier = Modifier
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                .fillMaxWidth(),
                        )
                        ExposedDropdownMenu(
                            expanded = personExpanded,
                            onDismissRequest = { personExpanded = false },
                        ) {
                            people.forEach { p ->
                                DropdownMenuItem(
                                    text = { Text(p.fullName) },
                                    onClick = {
                                        personId = p.id
                                        personExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = direction == GiftDirection.RECEIVED,
                        onClick = { direction = GiftDirection.RECEIVED },
                        label = { Text(strings.giftReceivedShort) },
                    )
                    FilterChip(
                        selected = direction == GiftDirection.GIVEN,
                        onClick = { direction = GiftDirection.GIVEN },
                        label = { Text(strings.giftGivenShort) },
                    )
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it.filter { c -> c.isDigit() } },
                    label = { Text("${strings.amount}, ${Money.currency.symbol}") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )

                ReceiptPhotoPicker(photoUri = photoUri, onPhotoChanged = { photoUri = it })
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val amount = amountText.toLongOrNull() ?: 0L
                    val pid = personId
                    if (pid != null && amount > 0) onConfirm(pid, direction, amount, photoUri)
                },
                enabled = personId != null && (amountText.toLongOrNull() ?: 0L) > 0,
            ) { Text(strings.add) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(strings.cancel) }
        },
    )
}
