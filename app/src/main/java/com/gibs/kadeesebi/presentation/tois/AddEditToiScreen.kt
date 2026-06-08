package com.gibs.kadeesebi.presentation.tois

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gibs.kadeesebi.presentation.common.EventIcons
import com.gibs.kadeesebi.presentation.i18n.LocalStrings
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditToiScreen(
    onDone: () -> Unit,
    viewModel: AddEditToiViewModel = hiltViewModel(),
) {
    val strings = LocalStrings.current
    val form by viewModel.form.collectAsStateWithLifecycle()
    val people by viewModel.people.collectAsStateWithLifecycle()
    val eventTypes by viewModel.eventTypes.collectAsStateWithLifecycle()
    val dateFmt = remember(strings.locale) { SimpleDateFormat("d MMMM yyyy", strings.locale) }
    val shortDateFmt = remember(strings.locale) { SimpleDateFormat("d MMM yyyy", strings.locale) }
    val timeFmt = remember(strings.locale) { SimpleDateFormat("HH:mm", strings.locale) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showReminderDatePicker by remember { mutableStateOf(false) }
    var showReminderTimePicker by remember { mutableStateOf(false) }
    var hostExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (form.id == null) strings.newToi else strings.editToi) })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(strings.eventType)
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                eventTypes.forEach { type ->
                    FilterChip(
                        selected = form.eventTypeId == type.id,
                        onClick = { viewModel.update { it.copy(eventTypeId = type.id) } },
                        leadingIcon = {
                            Icon(
                                EventIcons.icon(type.iconKey),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                            )
                        },
                        label = { Text(strings.eventTypeName(type)) },
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = form.isOwnToi,
                    onCheckedChange = { checked -> viewModel.update { it.copy(isOwnToi = checked) } },
                )
                Text(text = if (form.isOwnToi) strings.ownToiSwitch else strings.guestToiSwitch)
            }

            if (!form.isOwnToi) {
                val hostName = people.firstOrNull { it.id == form.hostPersonId }?.fullName ?: ""
                ExposedDropdownMenuBox(
                    expanded = hostExpanded,
                    onExpandedChange = { hostExpanded = it },
                ) {
                    OutlinedTextField(
                        value = hostName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(strings.whoseToi) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = hostExpanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(
                        expanded = hostExpanded,
                        onDismissRequest = { hostExpanded = false },
                    ) {
                        if (people.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(strings.addPeopleFirst) },
                                onClick = { hostExpanded = false },
                            )
                        }
                        people.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.fullName) },
                                onClick = {
                                    viewModel.update { it.copy(hostPersonId = p.id) }
                                    hostExpanded = false
                                },
                            )
                        }
                    }
                }
            }

            OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
                Text("${strings.datePrefix}: ${dateFmt.format(Date(form.date))}")
            }

            if (form.date > System.currentTimeMillis()) {
                Card(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(
                                checked = form.reminderEnabled,
                                onCheckedChange = { checked ->
                                    viewModel.update {
                                        it.copy(
                                            reminderEnabled = checked,
                                            reminderAt = if (checked && it.reminderAt <= System.currentTimeMillis()) {
                                                defaultReminderAt(it.date)
                                            } else {
                                                it.reminderAt
                                            },
                                        )
                                    }
                                },
                            )
                            Text(
                                text = strings.reminderSwitch,
                                modifier = Modifier.padding(start = 8.dp),
                            )
                        }
                        if (form.reminderEnabled) {
                            Text(strings.reminderHint, style = MaterialTheme.typography.bodySmall)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { showReminderDatePicker = true },
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text("${strings.reminderDateLabel}: ${shortDateFmt.format(Date(form.reminderAt))}")
                                }
                                OutlinedButton(
                                    onClick = { showReminderTimePicker = true },
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Text("${strings.reminderTimeLabel}: ${timeFmt.format(Date(form.reminderAt))}")
                                }
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                value = form.title,
                onValueChange = { v -> viewModel.update { it.copy(title = v) } },
                label = { Text(strings.eventTitle) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = form.place,
                onValueChange = { v -> viewModel.update { it.copy(place = v) } },
                label = { Text(strings.place) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = form.note,
                onValueChange = { v -> viewModel.update { it.copy(note = v) } },
                label = { Text(strings.notes) },
                modifier = Modifier.fillMaxWidth(),
            )

            Button(onClick = { viewModel.save(onDone) }, modifier = Modifier.fillMaxWidth()) {
                Text(strings.save)
            }

            if (form.id != null) {
                OutlinedButton(
                    onClick = { viewModel.delete(onDone) },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(strings.deleteToi, color = Color(0xFFC0392B)) }
            }
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState(initialSelectedDateMillis = form.date)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { ms ->
                        viewModel.update {
                            val newReminder = if (!it.reminderEnabled) defaultReminderAt(ms) else it.reminderAt
                            it.copy(date = ms, reminderAt = newReminder)
                        }
                    }
                    showDatePicker = false
                }) { Text(strings.ok) }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text(strings.cancel) }
            },
        ) { DatePicker(state = pickerState) }
    }

    if (showReminderDatePicker) {
        val reminderDateState = rememberDatePickerState(initialSelectedDateMillis = form.reminderAt)
        DatePickerDialog(
            onDismissRequest = { showReminderDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    reminderDateState.selectedDateMillis?.let { ms ->
                        viewModel.update { it.copy(reminderAt = combineDateKeepTime(ms, it.reminderAt)) }
                    }
                    showReminderDatePicker = false
                }) { Text(strings.ok) }
            },
            dismissButton = {
                TextButton(onClick = { showReminderDatePicker = false }) { Text(strings.cancel) }
            },
        ) { DatePicker(state = reminderDateState) }
    }

    if (showReminderTimePicker) {
        val cal = remember(form.reminderAt) { Calendar.getInstance().apply { timeInMillis = form.reminderAt } }
        val timeState = rememberTimePickerState(
            initialHour = cal.get(Calendar.HOUR_OF_DAY),
            initialMinute = cal.get(Calendar.MINUTE),
            is24Hour = true,
        )
        AlertDialog(
            onDismissRequest = { showReminderTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.update { it.copy(reminderAt = combineTime(it.reminderAt, timeState.hour, timeState.minute)) }
                    showReminderTimePicker = false
                }) { Text(strings.ok) }
            },
            dismissButton = {
                TextButton(onClick = { showReminderTimePicker = false }) { Text(strings.cancel) }
            },
            title = { Text(strings.reminderTimeLabel) },
            text = { TimePicker(state = timeState) },
        )
    }
}

private fun combineDateKeepTime(pickedUtcMillis: Long, baseMillis: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = pickedUtcMillis }
    val base = Calendar.getInstance().apply { timeInMillis = baseMillis }
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, utc.get(Calendar.YEAR))
        set(Calendar.MONTH, utc.get(Calendar.MONTH))
        set(Calendar.DAY_OF_MONTH, utc.get(Calendar.DAY_OF_MONTH))
        set(Calendar.HOUR_OF_DAY, base.get(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, base.get(Calendar.MINUTE))
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun combineTime(baseMillis: Long, hour: Int, minute: Int): Long =
    Calendar.getInstance().apply {
        timeInMillis = baseMillis
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
