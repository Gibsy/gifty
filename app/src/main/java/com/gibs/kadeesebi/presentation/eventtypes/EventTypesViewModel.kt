package com.gibs.kadeesebi.presentation.eventtypes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.presentation.common.EventIcons
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EventTypesViewModel @Inject constructor(
    private val eventTypeRepository: EventTypeRepository,
) : ViewModel() {

    val eventTypes = eventTypeRepository.observeEventTypes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList<EventType>())

    fun addEventType(name: String, iconKey: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            eventTypeRepository.upsert(
                EventType(
                    id = UUID.randomUUID().toString(),
                    name = trimmed,
                    iconKey = iconKey.ifBlank { EventIcons.DEFAULT_KEY },
                    builtInKey = null,
                    sortOrder = (eventTypes.value.maxOfOrNull { it.sortOrder } ?: 0) + 1,
                ),
            )
        }
    }

    fun deleteEventType(id: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch { onResult(eventTypeRepository.delete(id)) }
    }
}
