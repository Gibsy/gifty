package com.gibs.kadeesebi.presentation.tois

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import com.gibs.kadeesebi.presentation.reminders.EventReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

data class AddEditToiForm(
    val id: String? = null,
    val eventTypeId: String = "",
    val date: Long = System.currentTimeMillis(),
    val place: String = "",
    val isOwnToi: Boolean = false,
    val hostPersonId: String? = null,
    val note: String = "",
    val title: String = "",
    val reminderEnabled: Boolean = false,
    val reminderAt: Long = defaultReminderAt(System.currentTimeMillis()),
)

@HiltViewModel
class AddEditToiViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val toiRepository: ToiRepository,
    personRepository: PersonRepository,
    eventTypeRepository: EventTypeRepository,
    private val reminderScheduler: EventReminderScheduler,
) : ViewModel() {

    private val toiId: String? = savedStateHandle["toiId"]

    private val _form = MutableStateFlow(AddEditToiForm())
    val form = _form.asStateFlow()

    val people = personRepository.observePeople()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<Person>())

    val eventTypes = eventTypeRepository.observeEventTypes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<EventType>())

    init {
        if (toiId != null) {
            viewModelScope.launch {
                toiRepository.observeToi(toiId).collect { toi ->
                    if (toi != null) {
                        _form.value = AddEditToiForm(
                            id = toi.id,
                            eventTypeId = toi.eventTypeId,
                            date = toi.date,
                            place = toi.place.orEmpty(),
                            isOwnToi = toi.isOwnToi,
                            hostPersonId = toi.hostPersonId,
                            note = toi.note.orEmpty(),
                            title = toi.title.orEmpty(),
                            reminderEnabled = toi.reminderAt != null,
                            reminderAt = toi.reminderAt ?: defaultReminderAt(toi.date),
                        )
                    }
                }
            }
        } else {
            viewModelScope.launch {
                eventTypeRepository.observeEventTypes().collect { types ->
                    if (_form.value.eventTypeId.isBlank() && types.isNotEmpty()) {
                        _form.update { it.copy(eventTypeId = types.first().id) }
                    }
                }
            }
        }
    }

    fun update(transform: (AddEditToiForm) -> AddEditToiForm) = _form.update(transform)

    fun delete(onDeleted: () -> Unit) {
        val id = _form.value.id ?: return onDeleted()
        viewModelScope.launch {
            reminderScheduler.cancel(id)
            toiRepository.delete(id)
            onDeleted()
        }
    }

    fun save(onSaved: () -> Unit) {
        val f = _form.value
        val typeId = f.eventTypeId.ifBlank { eventTypes.value.firstOrNull()?.id ?: return }
        val id = f.id ?: UUID.randomUUID().toString()
        val reminderAt = if (f.reminderEnabled && f.reminderAt > System.currentTimeMillis()) f.reminderAt else null
        viewModelScope.launch {
            toiRepository.upsert(
                Toi(
                    id = id,
                    hostPersonId = if (f.isOwnToi) null else f.hostPersonId,
                    eventTypeId = typeId,
                    date = f.date,
                    place = f.place.ifBlank { null },
                    isOwnToi = f.isOwnToi,
                    note = f.note.ifBlank { null },
                    title = f.title.ifBlank { null },
                    reminderAt = reminderAt,
                ),
            )
            if (reminderAt != null) {
                reminderScheduler.schedule(id, reminderAt)
            } else {
                reminderScheduler.cancel(id)
            }
            onSaved()
        }
    }
}

internal fun defaultReminderAt(eventDateMillis: Long): Long {
    val candidate = Calendar.getInstance().apply {
        timeInMillis = eventDateMillis
        set(Calendar.HOUR_OF_DAY, 9)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val now = System.currentTimeMillis()
    return if (candidate > now) candidate else now + 60 * 60 * 1000
}
