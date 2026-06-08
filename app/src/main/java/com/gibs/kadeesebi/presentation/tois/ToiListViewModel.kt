package com.gibs.kadeesebi.presentation.tois

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gibs.kadeesebi.domain.model.EventType
import com.gibs.kadeesebi.domain.model.Person
import com.gibs.kadeesebi.domain.model.Toi
import com.gibs.kadeesebi.domain.repository.EventTypeRepository
import com.gibs.kadeesebi.domain.repository.PersonRepository
import com.gibs.kadeesebi.domain.repository.ToiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ToiListItem(
    val toi: Toi,
    val hostName: String?,
    val eventType: EventType?,
)

data class ToiListState(
    val items: List<ToiListItem> = emptyList(),
)

@HiltViewModel
class ToiListViewModel @Inject constructor(
    toiRepository: ToiRepository,
    personRepository: PersonRepository,
    eventTypeRepository: EventTypeRepository,
) : ViewModel() {

    val state = combine(
        toiRepository.observeTois(),
        personRepository.observePeople(),
        eventTypeRepository.observeEventTypes(),
    ) { tois, people, types ->
        val byId: Map<String, Person> = people.associateBy { it.id }
        val typeById: Map<String, EventType> = types.associateBy { it.id }
        ToiListState(
            items = tois.map { toi ->
                ToiListItem(
                    toi = toi,
                    hostName = toi.hostPersonId?.let { byId[it]?.fullName },
                    eventType = typeById[toi.eventTypeId],
                )
            },
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ToiListState())
}
